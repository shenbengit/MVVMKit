package com.shencoder.mvvmkit.ext

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shencoder.mvvmkit.http.bean.BaseResponse
import com.shencoder.mvvmkit.base.viewmodel.BaseViewModel
import com.shencoder.mvvmkit.http.ResultStatus
import com.shencoder.mvvmkit.util.toastInfo
import com.shencoder.mvvmkit.util.toastSuccess
import com.shencoder.mvvmkit.util.toastError
import com.shencoder.mvvmkit.util.toastWarning
import com.shencoder.mvvmkit.util.toastNormal
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
                onFailure(ResultStatus.onFailure(it.getCode(), it.getMsg()))
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

fun BaseViewModel<*>.toastError(text: String) = applicationContext.toastError(text)
fun BaseViewModel<*>.toastSuccess(text: String) = applicationContext.toastSuccess(text)
fun BaseViewModel<*>.toastInfo(text: String) = applicationContext.toastInfo(text)
fun BaseViewModel<*>.toastWarning(text: String) = applicationContext.toastWarning(text)
fun BaseViewModel<*>.toastNormal(text: String) = applicationContext.toastNormal(text)