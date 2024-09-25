@file:JvmName("LogExt")

package com.shencoder.mvvmkit.ext

import com.shencoder.mvvmkit.libEnvironment


/**
 *
 * @author Shenben
 * @date 2024/9/25 15:30
 * @description
 * @since
 */
inline val Any.TAG: String
    get() = this::class.java.simpleName

fun logV(tag: String, msg: Any) {
    libEnvironment.logV(tag) { msg }
}

fun logV(tag: String, msg: () -> Any) {
    libEnvironment.logV(tag, msg)
}

fun logD(tag: String, msg: Any) {
    libEnvironment.logD(tag) { msg }
}

fun logD(tag: String, msg: () -> Any) {
    libEnvironment.logD(tag, msg)
}

fun logI(tag: String, msg: Any) {
    libEnvironment.logI(tag) { msg }
}

fun logI(tag: String, msg: () -> Any) {
    libEnvironment.logI(tag, msg)
}

fun logW(tag: String, msg: Any) {
    libEnvironment.logW(tag) { msg }
}

fun logW(tag: String, msg: () -> Any) {
    libEnvironment.logW(tag, msg)
}

fun logE(tag: String, msg: Any) {
    libEnvironment.logE(tag) { msg }
}

fun logE(tag: String, msg: () -> Any) {
    libEnvironment.logE(tag, msg)
}