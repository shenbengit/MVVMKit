package com.shencoder.mvvmkit.http

/**
 *
 * @author  ShenBen
 * @date    2021/04/19 14:27
 * @email   714081644@qq.com
 */
sealed class ResultStatus<T> {

    companion object {
        fun <T> onSuccess(data: T?) = Success(data)
        fun onFailure(code: Int, msg: String) = Failure(code, msg)
        fun onError(throwable: Throwable) = Error(throwable)
    }

    /**
     * 请求成功
     */
    data class Success<T>(val data: T?) : ResultStatus<T>()

    /**
     * 请求失败
     */
    data class Failure(val code: Int, val msg: String) : ResultStatus<Nothing>()

    /**
     * 请求异常
     */
    data class Error(val throwable: Throwable) : ResultStatus<Nothing>()
}