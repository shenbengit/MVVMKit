package com.shencoder.mvvmkitdemo.http

import com.shencoder.mvvmkit.ext.logI
import com.shencoder.mvvmkit.http.BaseRetrofitClient
import com.shencoder.mvvmkit.http.interceptor.HttpLoggerInterceptor
import com.shencoder.mvvmkit.util.moshi.NullSafeKotlinJsonAdapterFactory
import com.shencoder.mvvmkit.util.moshi.NullSafeStandardJsonAdapters
import com.shencoder.mvvmkitdemo.ApiService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit


/**
 *
 * @author Shenben
 * @date 2024/9/29 14:42
 * @description
 * @since
 */
class RetrofitClient : BaseRetrofitClient() {

    companion object {
        private const val TAG = "RetrofitClient"
        private const val DEFAULT_MILLISECONDS: Long = 30
    }

    private lateinit var apiService: ApiService

    override fun generateOkHttpBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        val interceptor = HttpLoggerInterceptor { message -> logI(TAG) { message } }
        interceptor.level = HttpLoggerInterceptor.Level.BODY

        return builder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
    }

    override fun generateRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            val moshi = Moshi.Builder()
                .addLast(NullSafeKotlinJsonAdapterFactory())
                .addLast(NullSafeStandardJsonAdapters.FACTORY)
                .build()
            addConverterFactory(MoshiConverterFactory.create(moshi))
        }
    }

    /**
     * 动态修改Retrofit-baseUrl
     */
    fun setBaseUrl(baseUrl: String) {
        apiService = getApiService(ApiService::class.java, baseUrl, false)
    }

    fun getApiService(): ApiService {
        if (this::apiService.isInitialized.not()) {
            setBaseUrl("https://api.github.com/")
        }
        return apiService
    }

}