package com.shencoder.mvvmkit.ext

import androidx.lifecycle.lifecycleScope
import com.shencoder.mvvmkit.base.view.BaseSupportActivity
import com.shencoder.mvvmkit.http.ResultStatus
import com.shencoder.mvvmkit.http.bean.BaseResponse
import kotlinx.coroutines.CoroutineScope


/**
 *
 * @author Shenben
 * @date 2024/10/9 18:01
 * @description
 * @since
 */

/**
 * 网络请求
 * @param block 网络请求具体方法
 * @param onSuccess 请求成功结果回调
 * @param onFailure 请求失败结果回调（网络请求成功，但不是成功结果）
 * @param onError 请求异常结果回调
 * @param onComplete 整个请求流程结束回调
 * @param isShowLoadingDialog 是否显示LoadingDialog
 */
fun <T> BaseSupportActivity<*, *>.httpRequest(
    block: suspend CoroutineScope.() -> BaseResponse<T>,
    onSuccess: (ResultStatus.Success<T>) -> Unit = {},
    onFailure: (ResultStatus.Failure) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    onComplete: () -> Unit = {},
    isShowLoadingDialog: Boolean = true
) = lifecycleScope.httpRequest(block, {
    if (isShowLoadingDialog) {
        showLoadingDialog()
    }
}, onSuccess, onFailure, onError, {
    if (isShowLoadingDialog) {
        dismissLoadingDialog()
    }
    onComplete()
})


/**
 * IO请求
 */
fun <T> BaseSupportActivity<*, *>.request(
    block: suspend CoroutineScope.() -> T,
    onSuccess: (ResultStatus.Success<T>) -> Unit = {},
    onError: (ResultStatus.Error) -> Unit = {},
    onComplete: () -> Unit = {},
    isShowLoadingDialog: Boolean = true
) = lifecycleScope.request(block, {
    if (isShowLoadingDialog) {
        showLoadingDialog()
    }
}, onSuccess, onError, {
    if (isShowLoadingDialog) {
        dismissLoadingDialog()
    }
    onComplete()
})