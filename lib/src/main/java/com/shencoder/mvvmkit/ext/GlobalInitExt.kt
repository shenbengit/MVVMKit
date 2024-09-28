@file:JvmName("GlobalInit")

package com.shencoder.mvvmkit.ext

import com.shencoder.mvvmkit.di.appModule
import com.shencoder.mvvmkit.libEnvironment
import com.shencoder.mvvmkit.util.AppManager
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVHandler
import com.tencent.mmkv.MMKVLogLevel
import com.tencent.mmkv.MMKVRecoverStrategic
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
@JvmOverloads
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
@JvmOverloads
fun initMMKV(
    logLevel: MMKVLogLevel = if (libEnvironment.debug) MMKVLogLevel.LevelDebug else MMKVLogLevel.LevelWarning,
    mmkvPath: String = /*mmkv 默认地址*/AppManager.context.filesDir.absolutePath + "/mmkv",
    libLoader: MMKV.LibLoader? = null,
    handler: MMKVHandler? = null
) {
    val mmkvHandler = handler ?: object : MMKVHandler {
        val TAG = "MMKV"
        override fun onMMKVCRCCheckFail(mmapID: String?): MMKVRecoverStrategic {
            return MMKVRecoverStrategic.OnErrorDiscard
        }

        override fun onMMKVFileLengthError(mmapID: String?): MMKVRecoverStrategic {
            return MMKVRecoverStrategic.OnErrorDiscard
        }

        override fun wantLogRedirecting(): Boolean {
            return true
        }

        override fun mmkvLog(
            level: MMKVLogLevel,
            file: String?,
            line: Int,
            function: String?,
            message: String?
        ) {
            val msg = { "<$file:$line::$function> $message" }
            when (level) {
                MMKVLogLevel.LevelDebug -> {
                    logD(TAG, msg)
                }

                MMKVLogLevel.LevelInfo -> {
                    logI(TAG, msg)
                }

                MMKVLogLevel.LevelWarning -> {
                    logW(TAG, msg)
                }

                MMKVLogLevel.LevelError -> {
                    logE(TAG, msg)
                }

                MMKVLogLevel.LevelNone -> {
//                    logV(TAG) { msg }
                }
            }
        }
    }

    MMKV.initialize(AppManager.context, mmkvPath, libLoader, logLevel, mmkvHandler)
}

/**
 * 初始化Fragmentation
 * 可自行初始化
 * You can initialize it yourself
 *
 * @param stackViewMode [Fragmentation.NONE] [Fragmentation.SHAKE] [Fragmentation.BUBBLE]
 */
@JvmOverloads
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