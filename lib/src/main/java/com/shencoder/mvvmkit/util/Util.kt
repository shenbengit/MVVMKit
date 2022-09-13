package com.shencoder.mvvmkit.util

import android.content.Context
import android.os.Looper
import android.util.TypedValue
import java.util.*
import kotlin.math.roundToInt

/**
 *
 * @author  ShenBen
 * @date    2021/03/30 10:57
 * @email   714081644@qq.com
 */

val weekMap = mapOf(
    Calendar.SUNDAY to "星期天",
    Calendar.MONDAY to "星期一",
    Calendar.TUESDAY to "星期二",
    Calendar.WEDNESDAY to "星期三",
    Calendar.THURSDAY to "星期四",
    Calendar.FRIDAY to "星期五",
    Calendar.SATURDAY to "星期六"
)

fun Date.getWeekDay(): String {
    val calendar = Calendar.getInstance()
    calendar.time = this
    return weekMap[calendar.get(Calendar.DAY_OF_WEEK)] ?: ""
}

fun Context.dp2px(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp,
        resources.displayMetrics
    ).roundToInt()
}

fun Context.sp2px(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, dp,
        resources.displayMetrics
    ).roundToInt()
}

/** Returns {@code true} if called on the main thread, {@code false} otherwise. */
fun isOnMainThread() = Looper.getMainLooper() == Looper.myLooper()

/** Returns {@code true} if called on a background thread, {@code false} otherwise. */
fun isOnBackgroundThread() = isOnMainThread().not()