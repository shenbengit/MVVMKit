package com.shencoder.mvvmkitdemo.di

import com.shencoder.mvvmkitdemo.http.RetrofitClient
import org.koin.dsl.module


private val singleModule = module {
    single { RetrofitClient() }
}

val appModule = mutableListOf(singleModule).apply {
    addAll(viewModelModule)
}