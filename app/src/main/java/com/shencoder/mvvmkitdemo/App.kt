package com.shencoder.mvvmkitdemo

import android.app.Application
import android.util.Log
import com.shencoder.mvvmkit.InitEnvironment
import com.shencoder.mvvmkit.ext.globalInit
import com.shencoder.mvvmkit.ext.initFragmentation
import com.shencoder.mvvmkit.ext.initKoin
import com.shencoder.mvvmkit.ext.initMMKV
import com.shencoder.mvvmkit.ext.initToasty
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

        InitEnvironment.init(this, object : InitEnvironment.ConfigurationEnvironment {

            override val debug: Boolean
                get() = BuildConfig.DEBUG

            override val mmkvMode: Int
                get() = super.mmkvMode

            override val mmkvCryptKey: String?
                get() = super.mmkvCryptKey

            override fun logV(tag: String, msg: () -> Any) {
                Log.v(tag, msg().toString())
            }

            override fun logD(tag: String, msg: () -> Any) {
                if (debug) {
                    Log.d(tag, msg().toString())
                }
            }

            override fun logI(tag: String, msg: () -> Any) {
                Log.i(tag, msg().toString())
            }

            override fun logW(tag: String, msg: () -> Any) {
                Log.w(tag, msg().toString())
            }

            override fun logE(tag: String, msg: () -> Any) {
                Log.e(tag, msg().toString())
            }
        }) {
            val koinApplication =
                KoinAndroidApplication
                    .create(
                        this,
                        if (BuildConfig.DEBUG) Level.ERROR else Level.ERROR
                    )
                    .modules(appModule)
            globalInit(koinApplication)

//            initToasty()
//            initMMKV()
//            initFragmentation()
//            initKoin(koinApplication)
        }
    }
}