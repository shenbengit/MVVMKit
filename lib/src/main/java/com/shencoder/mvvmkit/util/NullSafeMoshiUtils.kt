package com.shencoder.mvvmkit.util

import com.shencoder.mvvmkit.util.moshi.NullSafeKotlinJsonAdapterFactory
import com.shencoder.mvvmkit.util.moshi.NullSafeStandardJsonAdapters
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type

/**
 *
 * 空安全的json解析功能
 * 字段为空时，解析成默认值
 * @date    2023/10/24 21:23
 *
 */
object NullSafeMoshiUtils {

    val moshi = Moshi.Builder()
        .addLast(NullSafeKotlinJsonAdapterFactory())
        .addLast(NullSafeStandardJsonAdapters.FACTORY)
        .build()

    @JvmStatic
    fun <T> fromJson(json: String, clazz: Class<T>): T? {
        val adapter = moshi.adapter(clazz)
        return adapter.fromJson(json)
    }

    @JvmSynthetic
    inline fun <reified T> fromJson(json: String): T? {
        val adapter = moshi.adapter(T::class.java)
        return adapter.fromJson(json)
    }

    @JvmStatic
    fun <T> fromJson(json: String, type: Type): T? {
        val adapter = moshi.adapter<T>(type)
        return adapter.fromJson(json)
    }

    @JvmStatic
    fun <T> fromJsonToList(json: String, clazz: Class<T>): List<T> {
        val types = Types.newParameterizedType(List::class.java, clazz)
        return fromJson(json, types) ?: emptyList()
    }

    inline fun <reified T> fromJsonToMap(json: String): Map<String, T> {
        val types = Types.newParameterizedType(Map::class.java, String::class.java, T::class.java)
        return fromJson(json, types) ?: emptyMap()
    }

    inline fun <reified T> fromJsonToList(json: String): List<T> {
        val types = Types.newParameterizedType(List::class.java, T::class.java)
        return fromJson(json, types) ?: emptyList()
    }

    inline fun <reified T> toJson(bean: T): String {
        val adapter = moshi.adapter(T::class.java)
        return adapter.toJson(bean)
    }
}