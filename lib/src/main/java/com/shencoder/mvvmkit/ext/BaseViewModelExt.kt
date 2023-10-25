package com.shencoder.mvvmkit.ext

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shencoder.mvvmkit.http.bean.BaseResponse
import com.shencoder.mvvmkit.base.viewmodel.BaseViewModel
import com.shencoder.mvvmkit.http.ResultStatus
import com.shencoder.mvvmkit.util.*
import kotlinx.coroutines.*

/**
 *
 * @author  ShenBen
 * @date    2021/04/19 14:23
 * @email   714081644@qq.com
 */

/**
 * 当页面销毁时会自动调用ViewModel的[ViewModel.onCleared]方法取消所有协程
 */
fun BaseViewModel<*>.launchOnUI(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch { block() }

fun BaseViewModel<*>.launchOnIO(block: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch(Dispatchers.IO) { block() }

/**
 * 在主线程延迟执行
 */
fun BaseViewModel<*>.launchOnUIDelay(delayMillis: Long, function: () -> Unit) = launchOnUI {
    delay(delayMillis)
    function()
}

/**
 * 网络请求
 * @param block 网络请求具体方法
 * @param onSuccess 请求成功结果回调
 * @param onFailure 请求失败结果回调（网络请求成功，但不是成功结果），一般公共的失败结果可在[BaseViewModelExt.defaultHttpRequestHandleFailure]中处理
 * @param onError 请求异常结果回调，一般公共的异常结果可在[BaseViewModelExt.defaultHttpRequestHandleError]中处理
 * @param isShowLoadingDialog 是否显示LoadingDialog
 * @param onComplete 整个请求流程结束回调
 */
fun <T> BaseViewModel<*>.httpRequest(
    block: suspend CoroutineScope.() -> BaseResponse<T>,
    onSuccess: (ResultStatus.Success<T?>) -> Unit = {},
    onFailure: (ResultStatus.Failure) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    isShowLoadingDialog: Boolean = true,
    onComplete: () -> Unit = {}
) = launchOnUI {
    if (isShowLoadingDialog) {
        showLoadingDialog()
    }
    val result = runCatching {
        withContext(Dispatchers.IO, block)
    }

    if (isShowLoadingDialog) {
        dismissLoadingDialog()
    }

    result.fold({
        if (it.isSuccess()) {
            onSuccess(ResultStatus.onSuccess(it.getResponseData()))
        } else {
            //处理公共的失败事件
            DefaultHttpRequestHandle.defaultHttpRequestFailure?.invoke(
                it.getResponseCode(),
                it.getResponseMsg()
            )
            onFailure(ResultStatus.onFailure(it.getResponseCode(), it.getResponseMsg()))
        }
    }, {
        //处理公共的异常事件
        DefaultHttpRequestHandle.defaultHttpRequestError?.invoke(it)
        onError(ResultStatus.onError(it))
    })

    onComplete()
}


/**
 * IO请求
 */
fun <T> BaseViewModel<*>.request(
    block: suspend CoroutineScope.() -> T,
    onSuccess: (ResultStatus.Success<T?>) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    isShowLoadingDialog: Boolean = true,
    onComplete: () -> Unit = {}
) = launchOnUI {
    if (isShowLoadingDialog) {
        showLoadingDialog()
    }
    val result = runCatching {
        withContext(Dispatchers.IO, block)
    }

    if (isShowLoadingDialog) {
        dismissLoadingDialog()
    }
    result.fold({
        onSuccess(ResultStatus.onSuccess(it))
    }, {
        onError(ResultStatus.onError(it))
    })

    onComplete()
}


fun BaseViewModel<*>.toastError(
    text: String,
    @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) = applicationContext.toastError(text, duration, withIcon)

fun BaseViewModel<*>.toastSuccess(
    text: String,
    @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) = applicationContext.toastSuccess(text, duration, withIcon)

fun BaseViewModel<*>.toastInfo(
    text: String,
    @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) = applicationContext.toastInfo(text, duration, withIcon)

fun BaseViewModel<*>.toastWarning(
    text: String,
    @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) = applicationContext.toastWarning(text, duration, withIcon)

fun BaseViewModel<*>.toastNormal(
    text: String,
    @ToastDuration duration: Int = Toast.LENGTH_SHORT
) = applicationContext.toastNormal(text, duration)