package com.shencoder.mvvmkit.base.viewmodel

import android.app.Application
import com.shencoder.mvvmkit.base.repository.BaseNothingRepository

/**
 * 如果不需要ViewModel处理逻辑，则可以使用[DefaultViewModel]代替
 *
 * @author  ShenBen
 * @date    2021/03/16 16:59
 * @email   714081644@qq.com
 */
class DefaultViewModel(application: Application, nothingRepository: BaseNothingRepository) :
    BaseViewModel<BaseNothingRepository>(application, nothingRepository)