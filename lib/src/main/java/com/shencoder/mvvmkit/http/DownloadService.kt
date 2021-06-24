package com.shencoder.mvvmkit.http

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

/**
 *
 * @author  ShenBen
 * @date    2021/04/14 10:50
 * @email   714081644@qq.com
 */
interface DownloadService {

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody

    @Streaming
    @GET
    suspend fun downloadFile(@Header("RANGE") start: String, @Url url: String): ResponseBody
}