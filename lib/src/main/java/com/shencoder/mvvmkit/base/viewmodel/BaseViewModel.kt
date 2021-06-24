package com.shencoder.mvvmkit.base.viewmodel

import android.app.Application
import android.content.Intent
import android.content.SharedPreferences
import androidx.annotation.MainThread
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.shencoder.mvvmkit.R
import com.shencoder.mvvmkit.base.repository.IRepository
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

    /**
     * 代替[SharedPreferences]
     */
    protected val mmkv: MMKV by inject()

    val baseLiveData by lazy { MutableLiveData<String>() }

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
    @MainThread
    fun showLoadingDialog() {
        baseLiveData.value = SHOW_LOADING_DIALOG
    }

    /**
     * 隐藏加载dialog
     */
    @MainThread
    fun dismissLoadingDialog() {
        baseLiveData.value = DISMISS_LOADING_DIALOG
    }

    /**
     * 回退
     */
    @MainThread
    fun backPressed() {
        baseLiveData.value = BACK_PRESSED
    }
}