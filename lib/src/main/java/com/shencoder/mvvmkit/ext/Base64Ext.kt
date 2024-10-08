package com.shencoder.mvvmkit.ext

import android.util.Base64

/**
 *
 * @date    2024/10/09 0:01
 */


/**
 * base64转ByteArray
 */
fun String.base64ToByteArray(flag: Int = Base64.DEFAULT): ByteArray {
    return Base64.decode(this, flag)
}

fun ByteArray.base64ToString(flag: Int = Base64.DEFAULT): String {
    return Base64.encodeToString(this, flag)
}