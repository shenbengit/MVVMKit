package com.shencoder.mvvmkit.ext

import android.view.View
import androidx.core.view.ViewCompat

fun View.isLayoutRTL(): Boolean {
    return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
}

private var triggerLastTime: Long = 0L

/**
 * 点击防抖动，默认500ms
 * tips：这个操作是全局的
 *
 * @param T
 * @param interval
 * @param block
 * @receiver
 */
fun <T : View> T.clickWithTrigger(interval: Long = 500, block: (T) -> Unit) {
    setOnClickListener {
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - triggerLastTime >= interval) {
            triggerLastTime = currentClickTime
            block(this)
        }
    }
}
