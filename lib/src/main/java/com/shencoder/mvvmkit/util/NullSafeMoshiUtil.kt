package com.shencoder.mvvmkit.util

import com.shencoder.mvvmkit.util.moshi.NullSafeKotlinJsonAdapterFactory
import com.shencoder.mvvmkit.util.moshi.NullSafeStandardJsonAdapters
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

/**
 *
 *
 * @date    2023/10/24 21:23
 *
 */
object NullSafeMoshiUtil {

    private var sMoshi = Moshi.Builder()
        .addLast(NullSafeKotlinJsonAdapterFactory())
        .addLast(NullSafeStandardJsonAdapters.FACTORY)
        .build()

    @JvmStatic
    fun <T> fromJson(json: String, clazz: Class<T>): T? {
        val adapter = sMoshi.adapter(clazz)
        return adapter.fromJson(json)
    }

    @JvmStatic
    fun <T> fromJson(json: String, type: Type): T? {
        val adapter = sMoshi.adapter<T>(type)
        return adapter.fromJson(json)
    }

    @JvmStatic
    fun toJson(bean: Any?): String {
        if (bean == null) {
            return "{}"
        }
        val adapter = sMoshi.adapter(bean.javaClass)
        return adapter.toJson(bean)
    }
}