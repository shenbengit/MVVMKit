package com.shencoder.mvvmkitdemo

import android.app.Application
import com.shencoder.mvvmkit.ext.globalInit
import com.shencoder.mvvmkitdemo.di.appModule
import org.koin.android.java.KoinAndroidApplication
import org.koin.core.logger.Level

/**
 *
 * @author  ShenBen
 * @date    2021/6/30 14:18
 * @email   714081644@qq.com
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val koinApplication =
            KoinAndroidApplication
                .create(
                    this,
                    if (BuildConfig.DEBUG) Level.ERROR else Level.ERROR
                )
                .modules(appModule)
        globalInit(BuildConfig.DEBUG, "", koinApplication)
    }
}