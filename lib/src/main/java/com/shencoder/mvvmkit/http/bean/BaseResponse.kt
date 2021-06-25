package com.shencoder.mvvmkit.http.bean

/**
 *
 * @author  ShenBen
 * @date    2020/10/26 11:53
 * @email   714081644@qq.com
 */
abstract class BaseResponse<T> {
    /**
     * 是否成功
     */
    abstract fun isSuccess(): Boolean

    /**
     * 返回状态码
     */
    abstract fun getResponseCode(): Int

    /**
     * 返回说明
     */
    abstract fun getResponseMsg(): String

    /**
     * 数据
     */
    abstract fun getResponseData(): T?
}