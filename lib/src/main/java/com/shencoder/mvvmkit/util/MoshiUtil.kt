package com.shencoder.mvvmkit.util

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

/**
 * json解析工具类
 *
 * @author ShenBen
 * @date 2020/09/28 16:34
 * @email 714081644@qq.com
 */
object MoshiUtil {

    private var sMoshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
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