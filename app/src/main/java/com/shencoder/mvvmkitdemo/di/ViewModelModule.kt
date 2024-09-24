package com.shencoder.mvvmkitdemo.di

import com.shencoder.mvvmkitdemo.MainViewModel
import com.shencoder.mvvmkitdemo.data.MainRemoteDataSource
import com.shencoder.mvvmkitdemo.data.MainRepository
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module


private val mainModule = module {
    factory { MainRemoteDataSource() }
    factoryOf(::MainRepository)
    viewModelOf(::MainViewModel)
}

val viewModelModule = arrayListOf(
    mainModule
)