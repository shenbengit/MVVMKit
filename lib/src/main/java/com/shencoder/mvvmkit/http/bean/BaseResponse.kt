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
     * 状态码
     */
    abstract fun getCode(): Int

    /**
     * 说明
     */
    abstract fun getMsg(): String

    /**
     * 数据
     */
    abstract fun getResponseData(): T?
}