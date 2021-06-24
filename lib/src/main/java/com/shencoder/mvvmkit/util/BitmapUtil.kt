package com.shencoder.mvvmkit.util

import android.graphics.Bitmap
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
