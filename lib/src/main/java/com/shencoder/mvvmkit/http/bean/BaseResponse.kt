package com.shencoder.mvvmkit.http.bean

/**
 *
 * @author  ShenBen
 * @date    2020/10/26 11:53
 * @email   714081644@qq.com
 */
interface BaseResponse<T> {
    /**
     * 是否成功
     */
    fun isSuccess(): Boolean

    /**
     * 返回状态码
     */
    fun getResponseCode(): Int

    /**
     * 返回说明
     */
    fun getResponseMsg(): String

    /**
     * 数据
     */
    fun getResponseData(): T?
}