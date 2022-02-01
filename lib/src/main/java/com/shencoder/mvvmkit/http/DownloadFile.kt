package com.shencoder.mvvmkit.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.BufferedSink
import okio.BufferedSource
import okio.buffer
import okio.sink
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

typealias Progress = (totalSize: Long, downloadSize: Long, progress: Float) -> Unit
typealias Success = (file: File) -> Unit
typealias Error = (error: Throwable) -> Unit

fun downloadFile(
    url: String,
    filePath: String,
    block: DownloadFile.() -> Unit
): DownloadFile {
    val downloadFile = DownloadFile(url, filePath)
    downloadFile.block()
    return downloadFile
}

class DownloadFile(private val url: String, private val filePath: String) : KoinComponent {
    private val downloadClient: DownloadRetrofitClient by inject()

    private var progress: Progress = { _, _, _ -> }
    private var success: Success = { }
    private var error: Error = { }

    fun progress(progress: Progress): DownloadFile = apply {
        this.progress = progress
    }

    fun success(success: Success): DownloadFile = apply {
        this.success = success
    }

    fun error(error: Error): DownloadFile = apply {
        this.error = error
    }

    private val downloadFlow = flow {
        var sink: BufferedSink? = null
        var source: BufferedSource? = null
        try {
            val responseBody = downloadClient.getDownloadService().downloadFile(url)
            val contentLength = responseBody.contentLength()
            val file = File(filePath)
            file.parentFile?.let {
                if (it.exists().not()) {
                    it.mkdirs()
                }
            }
            if (file.exists()) {
                file.delete()
            }

            sink = file.sink().buffer()
            val buffer = sink.buffer
            source = responseBody.source()
            var length: Long = 0
            var read: Long
            while (source.read(buffer, 200 * 1024.toLong()).also { read = it } != -1L) {
                length += read
                sink.emit()
                emit(
                    DownloadStatus.DownloadProcess(
                        contentLength,
                        length,
                        (length.toFloat() / contentLength)
                    )
                )
            }
            emit(DownloadStatus.DownloadSuccess(file))
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
                is DownloadStatus.DownloadProcess -> {
                    progress.invoke(it.totalSize, it.downloadSize, it.progress)
                }
                is DownloadStatus.DownloadSuccess -> {
                    success.invoke(it.file)
                }
                is DownloadStatus.DownloadError -> {
                    error.invoke(it.error)
                }
            }
        }
    }
}

sealed class DownloadStatus {
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
    ) :
        DownloadStatus()

    data class DownloadError(val error: Throwable) : DownloadStatus()

    data class DownloadSuccess(val file: File) : DownloadStatus()
}
