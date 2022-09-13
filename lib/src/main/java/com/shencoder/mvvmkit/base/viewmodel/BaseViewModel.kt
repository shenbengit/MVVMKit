package com.shencoder.mvvmkit.base.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.annotation.AnyThread
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.shencoder.mvvmkit.R
import com.shencoder.mvvmkit.base.repository.IRepository
import com.shencoder.mvvmkit.util.isOnMainThread
import com.tencent.mmkv.MMKV
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * 如果不需要ViewModel处理逻辑，则可以使用[DefaultViewModel]代替
 *
 * @author  ShenBen
 * @date    2021/03/16 16:41
 * @email   714081644@qq.com
 */
abstract class BaseViewModel<REPOSITORY : IRepository>(
    application: Application,
    protected val repo: REPOSITORY
) : AndroidViewModel(application), IViewModel, KoinComponent {
    companion object {
        /**
         * 显示加载dialog
         */
        const val SHOW_LOADING_DIALOG = "SHOW_LOADING_DIALOG"

        /**
         * 隐藏加载dialog
         */
        const val DISMISS_LOADING_DIALOG = "DISMISS_LOADING_DIALOG"

        /**
         * 回退
         */
        const val BACK_PRESSED = "BACK_PRESSED"
    }

    protected val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            this@BaseViewModel.handleMessage(msg)
        }
    }

    /**
     * 代替[SharedPreferences]
     */
    protected val mmkv: MMKV by inject()

    /**
     * 不对外暴露[MutableLiveData]
     */
    private val _baseLiveData = MutableLiveData<String>()

    /**
     * 为保证对外暴露的LiveData不可变
     * View层调用使用[LiveData]，避免违背单一数据源的原则
     */
    val baseLiveData: LiveData<String>
        get() = _baseLiveData

    val applicationContext: Application
        get() = getApplication()

    open fun getString(@StringRes resId: Int): String {
        return if (resId == 0) {
            applicationContext.getString(R.string.empty_str)
        } else {
            applicationContext.getString(resId)
        }
    }

    protected fun startActivity(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        applicationContext.startActivity(intent)
    }

    protected fun startService(intent: Intent) {
        applicationContext.startService(intent)
    }

    protected fun stopService(intent: Intent) {
        applicationContext.stopService(intent)
    }

    /**
     * 显示加载dialog
     */
    @AnyThread
    fun showLoadingDialog() {
        setBaseLiveDataValue(SHOW_LOADING_DIALOG)
    }

    /**
     * 隐藏加载dialog
     */
    @AnyThread
    fun dismissLoadingDialog() {
        setBaseLiveDataValue(DISMISS_LOADING_DIALOG)
    }

    /**
     * 回退
     */
    @AnyThread
    fun backPressed() {
        setBaseLiveDataValue(BACK_PRESSED)
    }

    /**
     * @see _baseLiveData
     */
    @SuppressLint("WrongThread")
    @AnyThread
    protected fun setBaseLiveDataValue(value: String) {
        if (isOnMainThread()) {
            _baseLiveData.value = value
        } else {
            //子线程使用Handler，解决LiveData.postValue()数据丢失问题
            mHandler.post {
                _baseLiveData.value = value
            }
        }
    }

    /**
     * @see mHandler
     */
    @CallSuper
    protected open fun handleMessage(msg: Message) {

    }

    @Deprecated(
        "Use 'this.applicationContext' instead",
        ReplaceWith("this.applicationContext"),
        level = DeprecationLevel.WARNING
    )
    override fun <T : Application> getApplication(): T {
        return super.getApplication()
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mHandler.removeCallbacksAndMessages(null)
    }
}