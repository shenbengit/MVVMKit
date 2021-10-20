package com.shencoder.mvvmkit.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 *
 * @author  ShenBen
 * @date    2021/04/21 17:43
 * @email   714081644@qq.com
 */

/**
 * Bitmap转Base64
 */
fun Bitmap.toBase64(quality: Int = 100): String {
    return ByteArrayOutputStream().use {
        compress(Bitmap.CompressFormat.JPEG, quality, it)
        Base64.encodeToString(it.toByteArray(), Base64.DEFAULT)
    }
}

/**
 * base64转ByteArray
 */
fun String.base64ToByteArray(flag: Int = Base64.DEFAULT): ByteArray {
    return Base64.decode(this, flag)
}

/**
 * base64转Bitmap
 */
fun String.base64ToBitmap(): Bitmap {
    val bytes = base64ToByteArray()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}