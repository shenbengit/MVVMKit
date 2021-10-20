package com.shencoder.mvvmkit.ext

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shencoder.mvvmkit.http.bean.BaseResponse
import com.shencoder.mvvmkit.base.viewmodel.BaseViewModel
import com.shencoder.mvvmkit.http.ResultStatus
import com.shencoder.mvvmkit.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
 * 网络请求
 */
fun <T> BaseViewModel<*>.httpRequest(
    block: suspend CoroutineScope.() -> BaseResponse<T>,
    onSuccess: (ResultStatus.Success<T?>) -> Unit = {},
    onFailure: (ResultStatus.Failure) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    isShowLoadingDialog: Boolean = true
) {
    launchOnUI {
        runCatching {
            if (isShowLoadingDialog) {
                showLoadingDialog()
            }
            withContext(Dispatchers.IO, block)
        }.onSuccess {
            if (isShowLoadingDialog) {
                dismissLoadingDialog()
            }
            if (it.isSuccess()) {
                onSuccess(ResultStatus.onSuccess(it.getResponseData()))
            } else {
                onFailure(ResultStatus.onFailure(it.getResponseCode(), it.getResponseMsg()))
            }
        }.onFailure {
            if (isShowLoadingDialog) {
                dismissLoadingDialog()
            }
            onError(ResultStatus.onError(it))
        }
    }
}

/**
 * IO请求
 */
fun <T> BaseViewModel<*>.request(
    block: suspend CoroutineScope.() -> T,
    onSuccess: (ResultStatus.Success<T?>) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    isShowLoadingDialog: Boolean = true
) {
    launchOnUI {
        runCatching {
            if (isShowLoadingDialog) {
                showLoadingDialog()
            }
            withContext(Dispatchers.IO, block)
        }.onSuccess {
            if (isShowLoadingDialog) {
                dismissLoadingDialog()
            }
            onSuccess(ResultStatus.onSuccess(it))
        }.onFailure {
            if (isShowLoadingDialog) {
                dismissLoadingDialog()
            }
            onError(ResultStatus.onError(it))
        }
    }
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