package com.shencoder.mvvmkitdemo

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.shencoder.mvvmkit.base.viewmodel.BaseViewModel
import com.shencoder.mvvmkit.ext.httpRequest
import com.shencoder.mvvmkit.ext.logI
import com.shencoder.mvvmkitdemo.data.MainRepository

/**
 *
 * @author  ShenBen
 * @date    2022/02/01 17:05
 * @email   714081644@qq.com
 */
class MainViewModel(
    application: Application,
    repo: MainRepository
) : BaseViewModel<MainRepository>(application, repo) {
    private companion object {
        private const val TAG = "MainViewModel"
    }

    override fun onCreate(owner: LifecycleOwner) {
        httpRequest({ repo.searchUsers("shenbengit") }, {
            val data = it.requireData
            logI(TAG) { "onCreate searchUsers: $data" }
        }, {

        }, {

        })
    }
}
