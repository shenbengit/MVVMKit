package com.shencoder.mvvmkit.ext

import android.content.Context
import com.shencoder.mvvmkit.di.appModule
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.LogLevel
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.shencoder.mvvmkit.R
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
 * @param debug debug
 * @param tag 打印日志的TAG
 * @param koinApplication
 */
fun Context.globalInit(debug: Boolean, tag: String, koinApplication: KoinApplication) {
    initLogger(tag, 2, if (debug) LogLevel.ALL else LogLevel.NONE)
    initToasty()
    initMMKV(if (debug) MMKVLogLevel.LevelDebug else MMKVLogLevel.LevelNone)
    initFragmentation(
        debug,
        R.anim.v_fragment_enter,
        R.anim.v_fragment_pop_exit,
        R.anim.v_fragment_pop_enter,
        R.anim.v_fragment_exit
    )
    initKoin(koinApplication)
}


/**
 * 初始化Logger
 * 可自行初始化
 * You can initialize it yourself
 *
 * @param tag tag
 * @param depth the number of stack trace elements we should log, 0 if no limitation
 * @param logLevel the log level
 */
fun initLogger(tag: String, depth: Int = 2, logLevel: Int = LogLevel.NONE) {
    val build = LogConfiguration.Builder()
        .enableStackTrace(depth)
        .logLevel(logLevel)
        .enableThreadInfo()
        .enableBorder()
        .tag(tag)
        .build()
    val androidPrinter = AndroidPrinter(true)
    XLog.init(build, androidPrinter)
}

/**
 * 初始化Toasty
 * 可自行初始化
 * You can initialize it yourself
 */
fun initToasty(allowQueue: Boolean = false) {
    Toasty.Config
        .getInstance()
        .allowQueue(allowQueue)
        .apply()
}

/**
 * 初始化mmvk
 * 可自行初始化
 * You can initialize it yourself
 */
fun Context.initMMKV(
    logLevel: MMKVLogLevel = MMKVLogLevel.LevelNone,
    mmkvPath: String? = getExternalFilesDir("mmkv")?.absolutePath
) {
    if (mmkvPath.isNullOrBlank()) {
        MMKV.initialize(this, logLevel)
    } else {
        MMKV.initialize(this, mmkvPath, logLevel)
    }
}

/**
 * 初始化Fragmentation
 * 可自行初始化
 * You can initialize it yourself
 */
fun initFragmentation(
    debug: Boolean = false,
    targetFragmentEnter: Int = 0,
    currentFragmentPopExit: Int = 0,
    currentFragmentPopEnter: Int = 0,
    targetFragmentExit: Int = 0
) {
    Fragmentation.builder()
        .debug(debug)
        .stackViewMode(if (debug) Fragmentation.BUBBLE else Fragmentation.NONE)
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