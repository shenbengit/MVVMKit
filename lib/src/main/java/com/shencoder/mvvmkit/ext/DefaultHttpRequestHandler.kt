package com.shencoder.mvvmkit.ext

/**
 *
 * @author  ShenBen
 * @date    2023/10/25 21:41
 * @email   714081644@qq.com
 */
object DefaultHttpRequestHandler {

    /**
     * 默认处理公共的失败结果
     */
    var defaultHttpRequestFailure: ((code: Int, msg: String) -> Unit)? = null

    /**
     * 默认处理异常的失败结果
     */
    var defaultHttpRequestError: ((error: Throwable) -> Unit)? = null
}