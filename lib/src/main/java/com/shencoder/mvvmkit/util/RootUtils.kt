package com.shencoder.mvvmkit.util

import android.os.Build
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader


/**
 *
 * @author Shenben
 * @date 2024/9/23 16:08
 * @description
 * @since
 */
object RootUtils {

    private val paths = arrayOf(
        "/system/app/Superuser.apk",
        "/sbin/su",
        "/system/bin/su",
        "/system/xbin/su",
        "/data/local/xbin/su",
        "/data/local/bin/su",
        "/system/sd/xbin/su",
        "/system/bin/failsafe/su",
        "/data/local/su",
        "/su/bin/su"
    )

    @JvmStatic
    fun isDeviceRooted(): Boolean {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3()
    }

    private fun checkRootMethod1(): Boolean {
        val buildTags = Build.TAGS
        return buildTags != null && buildTags.contains("test-keys")
    }

    private fun checkRootMethod2(): Boolean {
        for (path in paths) {
            if (File(path).exists()) {
                return true
            }
        }
        return false
    }

    private fun checkRootMethod3(): Boolean {
        var process: Process? = null
        var inputStream: InputStream? = null
        val rooted = try {
            process = Runtime.getRuntime().exec(arrayOf("/system/xbin/which", "su"))
            inputStream = process.inputStream
            val input = BufferedReader(InputStreamReader(inputStream))
            input.readLine() != null
        } catch (ignored: Throwable) {
            false
        }
        process?.destroy()
        inputStream?.close()
        return rooted
    }
}