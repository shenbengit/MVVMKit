package com.shencoder.mvvmkit

import android.app.Application
import com.shencoder.mvvmkit.network.NetworkObserverManager
import com.shencoder.mvvmkit.util.AppManager


/**
 *
 * @author Shenben
 * @date 2024/9/25 15:28
 * @description
 * @since
 */

internal val libEnvironment: InitEnvironment.ConfigurationEnvironment
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
     * @param block [GlobalInitExt]
     * @receiver
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

        fun logV(tag: String, msg: () -> Any)

        fun logD(tag: String, msg: () -> Any)

        fun logI(tag: String, msg: () -> Any)

        fun logW(tag: String, msg: () -> Any)

        fun logE(tag: String, msg: () -> Any)
    }
}