@file:JvmName("ShapeDrawableExt")

package com.shencoder.mvvmkit.ext

import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.Px


/**
 *
 * @author Shenben
 * @date 2024/10/12 20:05
 * @description
 * @since
 */

@JvmOverloads
fun shapeDrawable(
    shape: Int = GradientDrawable.RECTANGLE,
    @ColorInt solidColor: Int = Color.TRANSPARENT,
    @Px cornerRadius: Float = 0.0f,
    @Px cornerTopLeftRadius: Float = cornerRadius,
    @Px cornerTopRightRadius: Float = cornerRadius,
    @Px cornerBottomRightRadius: Float = cornerRadius,
    @Px cornerBottomLeftRadius: Float = cornerRadius,
    @Px strokeWidth: Int = 0,
    @ColorInt strokeColor: Int = Color.TRANSPARENT,
    @Px strokeDashWidth: Float = 0.0f,
    @Px strokeDashGap: Float = 0.0f,
    padding: Rect? = null,
    @Px width: Int = -1,
    @Px height: Int = -1
): GradientDrawable {
    return GradientDrawable().also { drawable ->
        drawable.shape = shape
        drawable.setColor(solidColor)

        if (shape == GradientDrawable.RECTANGLE) {
            // 仅矩形有效
            drawable.cornerRadius = cornerRadius
            if (cornerTopLeftRadius != cornerRadius
                || cornerTopRightRadius != cornerRadius
                || cornerBottomRightRadius != cornerRadius
                || cornerBottomLeftRadius != cornerRadius
            ) {
                drawable.cornerRadii = floatArrayOf(
                    cornerTopLeftRadius,
                    cornerTopLeftRadius,
                    cornerTopRightRadius,
                    cornerTopRightRadius,
                    cornerBottomRightRadius,
                    cornerBottomRightRadius,
                    cornerBottomLeftRadius,
                    cornerBottomLeftRadius
                )
            }
        }

        if (strokeWidth > 0 && strokeColor != Color.TRANSPARENT) {
            drawable.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap)
        }

        if (padding != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setPadding(padding.left, padding.top, padding.right, padding.bottom)
        }
        drawable.setSize(width, height)
    }
}

@JvmOverloads
fun gradientDrawable(
    shape: Int = GradientDrawable.RECTANGLE,
    colors: IntArray? = null,
    orientation: Orientation = Orientation.TOP_BOTTOM,
    gradientType: Int = GradientDrawable.LINEAR_GRADIENT,
    gradientCenterX: Float = 0.5f,
    gradientCenterY: Float = 0.5f,
    @Px gradientRadius: Float? = null,
    @Px cornerRadius: Float = 0.0f,
    @Px cornerTopLeftRadius: Float = cornerRadius,
    @Px cornerTopRightRadius: Float = cornerRadius,
    @Px cornerBottomRightRadius: Float = cornerRadius,
    @Px cornerBottomLeftRadius: Float = cornerRadius,
    @Px strokeWidth: Int = 0,
    @ColorInt strokeColor: Int = Color.TRANSPARENT,
    @Px strokeDashWidth: Float = 0.0f,
    @Px strokeDashGap: Float = 0.0f,
    padding: Rect? = null,
    @Px width: Int = -1,
    @Px height: Int = -1
): GradientDrawable {
    return GradientDrawable().also { drawable ->
        drawable.shape = shape
        drawable.orientation = orientation

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val useCenter = colors?.size == 3
            var offsets: FloatArray? = null
            if (useCenter && gradientType == GradientDrawable.LINEAR_GRADIENT) {
                offsets = FloatArray(3)
                offsets[0] = 0.0f
                // Since 0.5f is default value, try to take the one that isn't 0.5f
                offsets[1] = if (gradientCenterX != 0.5f) gradientCenterX else gradientCenterY
                offsets[2] = 1f
            }
            drawable.setColors(colors, offsets)
        } else {
            // Android 9及以下版本无法设置 offsets，gradientType == GradientDrawable.LINEAR_GRADIENT且需要兼容Android 9及以下版本时，需要使用xml
            drawable.colors = colors
        }
        drawable.gradientType = gradientType
        // gradientType != GradientDrawable.LINEAR_GRADIENT 时，无意义
        drawable.setGradientCenter(gradientCenterX, gradientCenterY)

        if (gradientType == GradientDrawable.RADIAL_GRADIENT && gradientRadius != null) {
            drawable.gradientRadius = gradientRadius
        }

        if (shape == GradientDrawable.RECTANGLE) {
            // 仅矩形有效
            drawable.cornerRadius = cornerRadius
            if (cornerTopLeftRadius != cornerRadius
                || cornerTopRightRadius != cornerRadius
                || cornerBottomRightRadius != cornerRadius
                || cornerBottomLeftRadius != cornerRadius
            ) {
                drawable.cornerRadii = floatArrayOf(
                    cornerTopLeftRadius,
                    cornerTopLeftRadius,
                    cornerTopRightRadius,
                    cornerTopRightRadius,
                    cornerBottomRightRadius,
                    cornerBottomRightRadius,
                    cornerBottomLeftRadius,
                    cornerBottomLeftRadius
                )
            }
        }
        if (strokeWidth > 0 && strokeColor != Color.TRANSPARENT) {
            drawable.setStroke(strokeWidth, strokeColor, strokeDashWidth, strokeDashGap)
        }

        if (padding != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawable.setPadding(padding.left, padding.top, padding.right, padding.bottom)
        }
        drawable.setSize(width, height)
    }
}