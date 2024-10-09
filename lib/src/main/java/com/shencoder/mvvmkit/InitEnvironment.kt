package com.shencoder.mvvmkit

import android.app.Application
import com.shencoder.mvvmkit.ext.globalInit
import com.shencoder.mvvmkit.ext.initFragmentation
import com.shencoder.mvvmkit.ext.initKoin
import com.shencoder.mvvmkit.ext.initMMKV
import com.shencoder.mvvmkit.ext.initToasty
import com.shencoder.mvvmkit.network.NetworkObserverManager
import com.shencoder.mvvmkit.util.AppManager
import com.tencent.mmkv.MMKV


/**
 *
 * @author Shenben
 * @date 2024/9/25 15:28
 * @description
 * @since
 */

internal inline val libEnvironment: InitEnvironment.ConfigurationEnvironment
    get() {
        return InitEnvironment.environment
    }

object InitEnvironment {

    private var _environment: ConfigurationEnvironment? = null

    internal val environment: ConfigurationEnvironment
        get() {
            val environment = _environment
            check(environment != null) {
                "you must be call init()."
            }
            return environment
        }

    /**
     * 初始化
     *
     * @param application
     * @param environment
     * @param block 调用[globalInit] 或者 [initToasty] [initMMKV] [initFragmentation] [initKoin]
     */
    @JvmStatic
    fun init(
        application: Application,
        environment: ConfigurationEnvironment,
        block: () -> Unit = {}
    ) {
        this._environment = environment
        AppManager.init(application)
        NetworkObserverManager.getInstance().init(application)

        block()
    }

    interface ConfigurationEnvironment {

        val debug: Boolean

        /**
         * mmkv mode
         * [MMKV.SINGLE_PROCESS_MODE] 单进程模式
         * [MMKV.MULTI_PROCESS_MODE] 多进程模式
         */
        val mmkvMode: Int
            get() = MMKV.SINGLE_PROCESS_MODE

        /**
         * mmkv 加密
         */
        val mmkvCryptKey: String?
            get() = null

        fun logV(tag: String, msg: () -> Any)

        fun logD(tag: String, msg: () -> Any)

        fun logI(tag: String, msg: () -> Any)

        fun logW(tag: String, msg: () -> Any)

        fun logE(tag: String, msg: () -> Any)
    }
}