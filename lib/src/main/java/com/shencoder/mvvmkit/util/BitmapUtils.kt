package com.shencoder.mvvmkit.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.shencoder.mvvmkit.ext.base64ToByteArray
import com.shencoder.mvvmkit.ext.base64ToString
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
        it.toByteArray().base64ToString()
    }
}


/**
 * base64转Bitmap
 */
fun String.base64ToBitmap(): Bitmap {
    val bytes = base64ToByteArray()
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}