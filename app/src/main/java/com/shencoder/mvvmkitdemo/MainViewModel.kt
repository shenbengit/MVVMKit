package com.shencoder.mvvmkitdemo

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import com.shencoder.mvvmkit.base.repository.BaseNothingRepository
import com.shencoder.mvvmkit.base.viewmodel.BaseViewModel
import com.shencoder.mvvmkit.ext.launchOnUI
import com.shencoder.mvvmkit.ext.launchOnUIDelay

/**
 *
 * @author  ShenBen
 * @date    2022/02/01 17:05
 * @email   714081644@qq.com
 */
class MainViewModel(
    application: Application,
    repo: BaseNothingRepository
) : BaseViewModel<BaseNothingRepository>(application, repo) {
    override fun onCreate(owner: LifecycleOwner) {
        launchOnUIDelay(1000L){

        }
        launchOnUI {

        }
    }
}
