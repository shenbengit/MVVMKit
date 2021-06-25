package com.shencoder.mvvmkit.http

import okhttp3.OkHttpClient
import retrofit2.Retrofit

/**
 * 单例模式
 * Singleton
 * [DownloadFile]
 *
 * @author  ShenBen
 * @date    2021/6/10 10:02
 * @email   714081644@qq.com
 */
class DownloadRetrofitClient {

    private companion object {
        private const val BASE_URL = "http://localhost:8080/"
    }

    private var okHttpClient = OkHttpClient()

    private var downloadService: DownloadService = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(BASE_URL)
        .build()
        .create(DownloadService::class.java)

    fun setOkHttpClient(client: OkHttpClient) {
        okHttpClient = client
        downloadService = Retrofit.Builder()
            .client(client)
            .baseUrl(BASE_URL)
            .build()
            .create(DownloadService::class.java)
    }

    fun getOkHttpClient() = okHttpClient

    fun getDownloadService() = downloadService
}