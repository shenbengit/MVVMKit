@file:JvmName("GlobalInit")

package com.shencoder.mvvmkit.ext

import com.shencoder.mvvmkit.di.appModule
import com.shencoder.mvvmkit.libEnvironment
import com.shencoder.mvvmkit.util.AppManager
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVLogLevel
import com.weikaiyun.fragmentation.Fragmentation
import es.dmoral.toasty.Toasty
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

/**
 *
 * @author  ShenBen
 * @date    2021/6/3 10:51
 * @email   714081644@qq.com
 */

/**
 * 快速一键初始化
 *
 * @param koinApplication
 */
fun globalInit(koinApplication: KoinApplication) {
    initToasty()
    initMMKV()
    initFragmentation()
    initKoin(koinApplication)
}


/**
 * 初始化Toasty
 * 可自行初始化
 * You can initialize it yourself
 */
fun initToasty(allowQueue: Boolean = true) {
    Toasty.Config
        .getInstance()
        .allowQueue(allowQueue)
        .apply()
}

/**
 * 初始化mmkv
 * 可自行初始化
 * You can initialize it yourself
 */
fun initMMKV(
    logLevel: MMKVLogLevel = if (libEnvironment.debug) MMKVLogLevel.LevelDebug else MMKVLogLevel.LevelNone,
    mmkvPath: String? = null
) {
    if (mmkvPath.isNullOrBlank()) {
        MMKV.initialize(AppManager.context, logLevel)
    } else {
        MMKV.initialize(AppManager.context, mmkvPath, logLevel)
    }
}

/**
 * 初始化Fragmentation
 * 可自行初始化
 * You can initialize it yourself
 *
 * @param stackViewMode [Fragmentation.NONE] [Fragmentation.SHAKE] [Fragmentation.BUBBLE]
 */
fun initFragmentation(
    stackViewMode: Int = if (libEnvironment.debug) Fragmentation.BUBBLE else Fragmentation.NONE,
    targetFragmentEnter: Int = 0,
    currentFragmentPopExit: Int = 0,
    currentFragmentPopEnter: Int = 0,
    targetFragmentExit: Int = 0
) {
    Fragmentation.builder()
        .debug(libEnvironment.debug)
        .stackViewMode(stackViewMode)
        .animation(
            targetFragmentEnter,
            currentFragmentPopExit,
            currentFragmentPopEnter,
            targetFragmentExit
        )
        .install()
}

/**
 * 初始化Koin
 * 必须使用这种方式初始化
 * You must initialize Koin in this way
 */
fun initKoin(koinApplication: KoinApplication) {
    koinApplication.modules(appModule)
    startKoin(koinApplication)
}