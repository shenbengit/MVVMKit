package com.shencoder.mvvmkit.http

import com.shencoder.mvvmkit.ext.httpRequest

/**
 *
 * @author  ShenBen
 * @date    2023/10/25 21:41
 * @email   714081644@qq.com
 */
object DefaultHttpRequestHandler {

    /**
     * 默认处理公共的失败结果
     * 如果返回true，则不会回调[httpRequest]里的[onFailure]函数
     */
    var defaultHttpRequestFailure: ((code: Int, msg: String) -> Boolean)? = null

    /**
     * 默认处理异常的失败结果
     */
    var defaultHttpRequestError: ((error: Throwable) -> Unit)? = null
}