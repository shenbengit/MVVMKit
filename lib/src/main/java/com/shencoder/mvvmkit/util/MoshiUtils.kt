package com.shencoder.mvvmkit.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

/**
 * json解析工具类
 *
 * @author ShenBen
 * @date 2020/09/28 16:34
 * @email 714081644@qq.com
 */
object MoshiUtils {

    val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
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

    inline fun <reified T> fromJsonToList(json: String): List<T> {
        val types = Types.newParameterizedType(List::class.java, T::class.java)
        return fromJson(json, types) ?: emptyList()
    }

    inline fun <reified T> fromJsonToMap(json: String): Map<String, T> {
        val types = Types.newParameterizedType(Map::class.java, String::class.java, T::class.java)
        return fromJson(json, types) ?: emptyMap()
    }

    inline fun <reified T> toJson(bean: T): String {
        val adapter = moshi.adapter(T::class.java)
        return adapter.toJson(bean)
    }

}