package com.shencoder.mvvmkitdemo.di

import org.koin.dsl.module


private val singleModule = module {

}

val appModule = mutableListOf(singleModule).apply {
    addAll(viewModelModule)
}