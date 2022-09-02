package com.shencoder.mvvmkitdemo

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.shencoder.mvvmkit.coil.BufferedSourceFetcher
import com.shencoder.mvvmkit.coil.ByteArrayFetcher
import com.shencoder.mvvmkit.ext.globalInit
import org.koin.android.java.KoinAndroidApplication
import org.koin.core.logger.Level

/**
 *
 * @author  ShenBen
 * @date    2021/6/30 14:18
 * @email   714081644@qq.com
 */
class App : Application(), ImageLoaderFactory {

    override fun onCreate() {
        super.onCreate()
        val koinApplication =
            KoinAndroidApplication
                .create(
                    this,
                    if (BuildConfig.DEBUG) Level.ERROR else Level.ERROR
                )
                .modules()
        globalInit(BuildConfig.DEBUG, "", koinApplication)
    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .componentRegistry {
                //Coil 图片加载框架不支持 ByteArray ，需要自己实现
                add(ByteArrayFetcher())
                //直接使用网络流显示图片
                add(BufferedSourceFetcher())
            }
            .build()
    }
}