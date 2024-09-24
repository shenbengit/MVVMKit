package com.shencoder.mvvmkit.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import okio.BufferedSink
import okio.BufferedSource
import okio.buffer
import okio.sink
import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

/**
 * 文件下载工具
 * 自行处理文件读取权限
 *
 * @author  ShenBen
 * @date    2021/04/13 18:09
 * @email   714081644@qq.com
 */

fun downloadFile(
    url: String,
    filePath: String,
    md5: String = "",
    block: DownloadFile.() -> Unit
): DownloadFile {
    val downloadFile = DownloadFile(url, filePath, md5)
    downloadFile.block()
    return downloadFile
}

open class DownloadFile(
    private val url: String,
    private val filePath: String,
    /**
     * 为空时不校验md5
     */
    private val md5: String = ""
) : KoinComponent {

    private val downloadClient: DownloadRetrofitClient by inject()

    private var start: (() -> Unit)? = null
    private var progress: ((totalSize: Long, downloadSize: Long, progress: Float) -> Unit)? = null
    private var success: ((file: File, fileMd5: String, md5VerifySuccess: Boolean) -> Unit)? = null
    private var error: ((error: Throwable) -> Unit)? = null

    fun start(start: () -> Unit) = apply {
        this.start = start
    }

    fun progress(progress: (totalSize: Long, downloadSize: Long, progress: Float) -> Unit) = apply {
        this.progress = progress
    }

    fun success(success: (file: File, fileMd5: String, md5VerifySuccess: Boolean) -> Unit) = apply {
        this.success = success
    }

    fun error(error: (error: Throwable) -> Unit) = apply {
        this.error = error
    }

    protected open suspend fun downloadFile(url: String): ResponseBody {
        return downloadClient.getDownloadService().downloadFile(url)
    }

    private val downloadFlow = flow {
        var sink: BufferedSink? = null
        var source: BufferedSource? = null
        try {
            emit(DownloadStatus.DownloadStart)

            val responseBody = downloadFile(url)
            val contentLength = responseBody.contentLength()
            val file = File(filePath)
            file.parentFile?.mkdirs()
            if (file.exists()) {
                file.delete()
            }
            file.createNewFile()

            sink = file.sink().buffer()
            val buffer = sink.buffer
            source = responseBody.source()
            var length: Long = 0
            var read: Long
            val byteCount = 200 * 1024.toLong()
            var time = 0L
            while (source.read(buffer, byteCount).also { read = it } != -1L) {
                length += read
                sink.emit()
                val timeMillis = System.currentTimeMillis()
                if (timeMillis - time >= 200L) {
                    // 200ms 回调一次，避免频繁回调
                    time = timeMillis
                    emit(
                        DownloadStatus.DownloadProcess(
                            contentLength,
                            length,
                            (length.toFloat() / contentLength)
                        )
                    )
                } else {
                    if (length == contentLength) {
                        // 确保下载完成时回调
                        emit(DownloadStatus.DownloadProcess(contentLength, length, 1f))
                    }
                }
            }
            var fileMd5 = ""
            if (md5.isNotBlank()) {
                // 校验md5
                fileMd5 = String(Hex.encodeHex(DigestUtils.md5(file.inputStream())))
            }
            emit(DownloadStatus.DownloadSuccess(file, fileMd5, fileMd5 == md5))
        } catch (t: Throwable) {
            emit(DownloadStatus.DownloadError(t))
        } finally {
            sink?.close()
            source?.close()
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 开始执行下载任务
     */
    suspend fun startDownload() {
        downloadFlow.collect {
            when (it) {
                is DownloadStatus.DownloadStart -> {
                    start?.invoke()
                }

                is DownloadStatus.DownloadProcess -> {
                    progress?.invoke(it.totalSize, it.downloadSize, it.progress)
                }

                is DownloadStatus.DownloadSuccess -> {
                    success?.invoke(it.file, it.fileMd5, it.md5VerifySuccess)
                }

                is DownloadStatus.DownloadError -> {
                    error?.invoke(it.error)
                }
            }
        }
    }

}

sealed class DownloadStatus {

    data object DownloadStart : DownloadStatus()

    data class DownloadProcess(
        /**
         * 文件总大小
         */
        val totalSize: Long,
        /**
         * 已下载文件大小
         */
        val downloadSize: Long,
        /**
         * 下载进度[0-1]
         */
        val progress: Float
    ) : DownloadStatus()

    data class DownloadError(val error: Throwable) : DownloadStatus()

    data class DownloadSuccess(val file: File, val fileMd5: String, val md5VerifySuccess: Boolean) :
        DownloadStatus()
}