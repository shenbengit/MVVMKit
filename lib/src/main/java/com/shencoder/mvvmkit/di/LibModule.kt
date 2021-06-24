package com.shencoder.mvvmkit.di

import com.shencoder.mvvmkit.base.repository.BaseNothingRepository
import com.shencoder.mvvmkit.base.viewmodel.DefaultViewModel
import com.shencoder.mvvmkit.http.DownloadRetrofitClient
import com.tencent.mmkv.MMKV
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 *
 * @author  ShenBen
 * @date    2021/03/16 17:01
 * @email   714081644@qq.com
 */

private val defaultModule = module {
    factory { BaseNothingRepository() }
    viewModel { DefaultViewModel(get(), get()) }
}

private val singleModule = module {
    single { MMKV.defaultMMKV() }
    single { DownloadRetrofitClient() }
}

internal val appModule = listOf(defaultModule, singleModule)