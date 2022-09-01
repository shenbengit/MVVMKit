package com.shencoder.mvvmkit.coil

import android.graphics.Bitmap
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation

/**
 * A [Transformation] that converts an image to shades of gray.
 *
 * @author  ShenBen
 * @date    2022/09/01 22:48
 * @email   714081644@qq.com
 */
class GrayscaleTransformation : Transformation {

    override val cacheKey: String = this::class.java.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
        paint.colorFilter = COLOR_FILTER
        val output =
            createBitmap(input.width, input.height, input.config ?: Bitmap.Config.ARGB_8888)
        output.applyCanvas {
            drawBitmap(input, 0f, 0f, paint)
        }
        return output
    }

    override fun equals(other: Any?) = other is GrayscaleTransformation

    override fun hashCode() = javaClass.hashCode()

    override fun toString() = "GrayscaleTransformation()"

    private companion object {
        val COLOR_FILTER = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
    }
}