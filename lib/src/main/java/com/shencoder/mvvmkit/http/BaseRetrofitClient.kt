package com.shencoder.mvvmkit.http

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * 自定义类并继承此类，设计成单例模式
 *
 * @author ShenBen
 * @date 2019/8/21 9:21
 * @email 714081644@qq.com
 */
abstract class BaseRetrofitClient {

    abstract fun generateOkHttpBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder

    open fun generateRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder{
        return builder
    }

    fun <T> getApiService(service: Class<T>, baseUrl: String): T {
        //默认添加对Moshi的支持
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(generateOkHttpBuilder(OkHttpClient.Builder()).build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))

        return generateRetrofitBuilder(retrofitBuilder)
            .build().create(service)
    }
}
