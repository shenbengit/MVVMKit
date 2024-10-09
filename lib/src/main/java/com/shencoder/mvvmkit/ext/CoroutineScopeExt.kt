package com.shencoder.mvvmkit.ext

import com.shencoder.mvvmkit.http.DefaultHttpRequestHandler
import com.shencoder.mvvmkit.http.ResultStatus
import com.shencoder.mvvmkit.http.bean.BaseResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * @author  ShenBen
 * @date    2023/10/25 21:40
 * @email   714081644@qq.com
 */

/**
 * 网络请求
 * @param block 网络请求具体方法
 * @param onStart 请求成功开始回调
 * @param onSuccess 请求成功结果回调
 * @param onFailure 请求失败结果回调（网络请求成功，但不是成功结果），一般公共的失败结果可在[DefaultHttpRequestHandler.defaultHttpRequestFailure]中处理
 * @param onError 请求异常结果回调，一般公共的异常结果可在[DefaultHttpRequestHandler.defaultHttpRequestError]中处理
 * @param onComplete 整个请求流程结束回调
 */
fun <T> CoroutineScope.httpRequest(
    block: suspend CoroutineScope.() -> BaseResponse<T>,
    onStart: () -> Unit = {},
    onSuccess: (ResultStatus.Success<T>) -> Unit = {},
    onFailure: (ResultStatus.Failure) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    onComplete: () -> Unit = {}
) = launch(Dispatchers.Main) {
    onStart()

    val result = runCatching { block() }

    result.fold({
        if (it.isSuccess()) {
            onSuccess(ResultStatus.onSuccess(it.getResponseData()))
        } else {
            //处理公共的失败事件
            val handled = DefaultHttpRequestHandler.defaultHttpRequestFailure?.invoke(
                it.getResponseCode(),
                it.getResponseMsg()
            ) ?: false
            if (!handled) {
                // 如果公共事件处理了，则不回调
                onFailure(ResultStatus.onFailure(it.getResponseCode(), it.getResponseMsg()))
            }
        }
    }, {
        //处理公共的异常事件
        DefaultHttpRequestHandler.defaultHttpRequestError?.invoke(it)
        onError(ResultStatus.onError(it))
    })

    onComplete()
}

fun <T> CoroutineScope.request(
    block: suspend CoroutineScope.() -> T,
    onStart: () -> Unit = {},
    onSuccess: (ResultStatus.Success<T>) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    onComplete: () -> Unit = {}
) = launch(Dispatchers.Main) {
    onStart()

    val result = runCatching {
        withContext(Dispatchers.IO, block)
    }
    result.fold({
        onSuccess(ResultStatus.onSuccess(it))
    }, {
        onError(ResultStatus.onError(it))
    })

    onComplete()
}