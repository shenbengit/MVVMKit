package com.shencoder.mvvmkit.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Process

/**
 *
 *
 * @date 2022/11/29 20:10
 * @description
 * @since
 */
object ProcessUtils {

    private const val TAG = "ProcessUtil"

    /**
     * 获取当前进程的名称
     * Gets the name of the current process
     */
    @JvmStatic
    fun getCurrentProcessName(context: Context): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            Application.getProcessName()
        } else {
            getCurrentProcessNameByActivityThread()
                ?: getCurrentProcessNameByActivityManager(context)
        }
    }

    /**
     * 通过反射[ActivityThread.currentProcessName]获取当前进程名称
     * Get the current process name by reflection [ActivityThread.currentProcessName]
     */
    private fun getCurrentProcessNameByActivityThread(): String? {
        return try {
            val clazz =
                Class.forName(
                    "android.app.ActivityThread",
                    false,
                    Application::class.java.classLoader
                )
            val method = clazz.getMethod("currentProcessName")
            val invoke = method.invoke(null)
            if (invoke is String) {
                invoke
            } else {
                null
            }
        } catch (e: Throwable) {
            null
        }
    }

    private fun getCurrentProcessNameByActivityManager(context: Context): String? {
        val pid = Process.myPid()
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val processes = manager.runningAppProcesses ?: return null
        for (process in processes) {
            if (process.pid == pid) {
                return process.processName
            }
        }
        return null
    }
}
