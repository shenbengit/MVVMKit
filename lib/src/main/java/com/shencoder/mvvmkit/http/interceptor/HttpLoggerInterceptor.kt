package com.shencoder.mvvmkit.http.interceptor


import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.util.TreeSet
import java.util.concurrent.TimeUnit
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.http.promisesBody
import okhttp3.internal.platform.Platform
import okio.Buffer
import okio.GzipSource
import java.io.EOFException

/**
 * An OkHttp interceptor which logs request and response information. Can be applied as an
 * [application interceptor][OkHttpClient.interceptors] or as a [OkHttpClient.networkInterceptors].
 *
 * The format of the logs created by this class should not be considered stable and may
 * change slightly between releases. If you need a stable logging format, use your own interceptor.
 *
 * @author Shenben
 * @date 2023/11/1 14:58
 * @description
 * @since
 */
class HttpLoggerInterceptor @JvmOverloads constructor(
    private val logger: Logger = Logger.DEFAULT
) : Interceptor {

    @Volatile
    private var headersToRedact = emptySet<String>()

    @Volatile
    private var omitBodyContentTypes = setOf("multipart/form-data", "image/")

    @set:JvmName("level")
    @Volatile
    var level = Level.NONE

    enum class Level {
        /** No logs. */
        NONE,

        /**
         * Logs request and response lines.
         *
         * Example:
         * ```
         * --> POST /greeting http/1.1 (3-byte body)
         *
         * <-- 200 OK (22ms, 6-byte body)
         * ```
         */
        BASIC,

        /**
         * Logs request and response lines and their respective headers.
         *
         * Example:
         * ```
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         * <-- END HTTP
         * ```
         */
        HEADERS,

        /**
         * Logs request and response lines and their respective headers and bodies (if present).
         *
         * Example:
         * ```
         * --> POST /greeting http/1.1
         * Host: example.com
         * Content-Type: plain/text
         * Content-Length: 3
         *
         * Hi?
         * --> END POST
         *
         * <-- 200 OK (22ms)
         * Content-Type: plain/text
         * Content-Length: 6
         *
         * Hello!
         * <-- END HTTP
         * ```
         */
        BODY
    }

    private companion object {
        private fun Buffer.isProbablyUtf8(): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = size.coerceAtMost(64)
                copyTo(prefix, 0, byteCount)
                for (i in 0 until 16) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (_: EOFException) {
                return false // Truncated UTF-8 sequence.
            }
        }
    }

    fun interface Logger {
        fun log(message: String)

        companion object {
            /** A [Logger] defaults output appropriate for the current platform. */
            @JvmField
            val DEFAULT: Logger = DefaultLogger()

            private class DefaultLogger : Logger {
                override fun log(message: String) {
                    Platform.get().log(message)
                }
            }
        }
    }

    fun redactHeader(name: String) {
        val newHeadersToRedact = TreeSet(String.CASE_INSENSITIVE_ORDER)
        newHeadersToRedact += headersToRedact
        newHeadersToRedact += name
        headersToRedact = newHeadersToRedact
    }

    fun setOmitBodyContentTypes(contentTypes: Set<String>) {
        omitBodyContentTypes = contentTypes
    }

    fun addOmitBodyContentType(contentType: String) {
        val newOmitBodyContentTypes = TreeSet(String.CASE_INSENSITIVE_ORDER)
        newOmitBodyContentTypes += omitBodyContentTypes
        newOmitBodyContentTypes += contentType
        omitBodyContentTypes = newOmitBodyContentTypes
    }

    /**
     * Sets the level and returns this.
     *
     * This was deprecated in OkHttp 4.0 in favor of the [level] val. In OkHttp 4.3 it is
     * un-deprecated because Java callers can't chain when assigning Kotlin vals. (The getter remains
     * deprecated).
     */
    fun setLevel(level: Level) = apply {
        this.level = level
    }

    @JvmName("-deprecated_level")
    @Deprecated(
        message = "moved to var",
        replaceWith = ReplaceWith(expression = "level"),
        level = DeprecationLevel.ERROR
    )
    fun getLevel(): Level = level

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val level = this.level

        val request = chain.request()
        if (level == Level.NONE) {
            return chain.proceed(request)
        }

        val logBody = level == Level.BODY
        val logHeaders = logBody || level == Level.HEADERS

        val requestBody = request.body

        val connection = chain.connection()

        val logBuilder = StringBuilder()
        logBuilder.append("--> ${request.method} ${request.url}${if (connection != null) " " + connection.protocol() else ""}")

        if (!logHeaders && requestBody != null) {
            logBuilder.appendLine(" (${requestBody.contentLength()}-byte body)")
        }

        logBuilder.appendLine()

        if (logHeaders) {
            val headers = request.headers

            if (requestBody != null) {
                // Request body headers are only present when installed as a network interceptor. When not
                // already present, force them to be included (if available) so their values are known.
                requestBody.contentType()?.let {
                    if (headers["Content-Type"] == null) {
                        logBuilder.appendLine("Content-Type: $it")
                    }
                }
                if (requestBody.contentLength() != -1L) {
                    if (headers["Content-Length"] == null) {
                        logBuilder.appendLine("Content-Length: ${requestBody.contentLength()}")
                    }
                }
            }
            logBuilder.appendLine("Request Headers: {")
            for (i in 0 until headers.size) {
                logHeader(logBuilder, headers, i)
            }
            logBuilder.appendLine("}")

            if (!logBody || requestBody == null) {
                logBuilder.appendLine("--> END ${request.method}")
            } else if (bodyHasUnknownEncoding(request.headers)) {
                logBuilder.appendLine("--> END ${request.method} (encoded body omitted)")
            } else if (requestBody.isDuplex()) {
                logBuilder.appendLine("--> END ${request.method} (duplex request body omitted)")
            } else if (requestBody.isOneShot()) {
                logBuilder.appendLine("--> END ${request.method} (one-shot body omitted)")
            } else {
                val buffer = Buffer()
                requestBody.writeTo(buffer)

                val contentType = requestBody.contentType()
                val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8
                if (contentType != null && omitBodyContentTypes.any {
                        contentType.toString().startsWith(it)
                    }) {
                    logBuilder.appendLine("--> END ${request.method} (content type $contentType excluded, ${requestBody.contentLength()}-byte body omitted)")
                } else if (buffer.isProbablyUtf8()) {
                    logBuilder.appendLine("Request Body:")
                    logBuilder.appendLine(buffer.readString(charset))
                    logBuilder.appendLine("--> END ${request.method} (${requestBody.contentLength()}-byte body)")
                } else {
                    logBuilder.appendLine(
                        "--> END ${request.method} (binary ${requestBody.contentLength()}-byte body omitted)"
                    )
                }
            }
        }

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            logBuilder.appendLine("<-- HTTP FAILED: $e")
            logger.log(logBuilder.toString())
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body!!
        val contentLength = responseBody.contentLength()
        val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
        logBuilder.appendLine(
            "<-- ${response.code}${if (response.message.isEmpty()) "" else ' ' + response.message} ${response.request.url} (${tookMs}ms${if (!logHeaders) ", $bodySize body" else ""})"
        )

        if (logHeaders) {
            val headers = response.headers
            logBuilder.appendLine("Response Headers: {")
            for (i in 0 until headers.size) {
                logHeader(logBuilder, headers, i)
            }
            logBuilder.appendLine("}")

            if (!logBody || !response.promisesBody()) {
                logBuilder.appendLine("<-- END HTTP")
            } else if (bodyHasUnknownEncoding(response.headers)) {
                logBuilder.appendLine("<-- END HTTP (encoded body omitted)")
            } else {
                val source = responseBody.source()
                source.request(Long.MAX_VALUE) // Buffer the entire body.
                var buffer = source.buffer

                var gzippedLength: Long? = null
                if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                    gzippedLength = buffer.size
                    GzipSource(buffer.clone()).use { gzippedResponseBody ->
                        buffer = Buffer()
                        buffer.writeAll(gzippedResponseBody)
                    }
                }

                val contentType = responseBody.contentType()
                val charset: Charset = contentType?.charset(UTF_8) ?: UTF_8
                if (contentType != null && omitBodyContentTypes.any {
                        contentType.toString().startsWith(it)
                    }) {
                    logBuilder.appendLine("<-- END HTTP (content type $contentType excluded, binary ${buffer.size}-byte body omitted)")
                    logger.log(logBuilder.toString())
                    return response
                }

                if (!buffer.isProbablyUtf8()) {
                    logBuilder.appendLine("<-- END HTTP (binary ${buffer.size}-byte body omitted)")
                    logger.log(logBuilder.toString())
                    return response
                }

                if (contentLength != 0L) {
                    logBuilder.appendLine("Response Body:")
                    logBuilder.appendLine(buffer.clone().readString(charset))
                }

                if (gzippedLength != null) {
                    logBuilder.appendLine("<-- END HTTP (${buffer.size}-byte, $gzippedLength-gzipped-byte body)")
                } else {
                    logBuilder.appendLine("<-- END HTTP (${buffer.size}-byte body)")
                }
            }
        }

        logger.log(logBuilder.toString())
        return response
    }

    private fun logHeader(builder: StringBuilder, headers: Headers, i: Int) {
        val value = if (headers.name(i) in headersToRedact) "██" else headers.value(i)
        builder.appendLine(headers.name(i) + ": " + value)
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }
}