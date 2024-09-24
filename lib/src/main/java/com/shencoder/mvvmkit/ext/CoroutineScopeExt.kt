package com.shencoder.mvvmkit.ext

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

fun <T> CoroutineScope.httpRequest(
    block: suspend CoroutineScope.() -> BaseResponse<T>,
    onSuccess: (ResultStatus.Success<T?>) -> Unit = {},
    onFailure: (ResultStatus.Failure) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    onComplete: () -> Unit = {}
) = launch(Dispatchers.Main) {
    val result = runCatching { block() }

    result.fold({
        if (it.isSuccess()) {
            onSuccess(ResultStatus.onSuccess(it.getResponseData()))
        } else {
            //处理公共的失败事件
            DefaultHttpRequestHandler.defaultHttpRequestFailure?.invoke(
                it.getResponseCode(),
                it.getResponseMsg()
            )
            onFailure(ResultStatus.onFailure(it.getResponseCode(), it.getResponseMsg()))
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
    onSuccess: (ResultStatus.Success<T?>) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    onComplete: () -> Unit = {}
) = launch(Dispatchers.Main) {
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