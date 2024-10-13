@file:JvmName("ViewExt")

package com.shencoder.mvvmkit.ext

import android.view.View
import androidx.core.view.ViewCompat

fun View.isLayoutRTL(): Boolean {
    return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
}

private var triggerLastTime: Long = 0L

var DEFAULT_CLICK_INTERVAL = 300L


/**
 * 点击防抖动，默认500ms
 * tips：这个操作是全局的
 *
 * @param T
 * @param interval
 * @param block
 * @receiver
 */
fun <T : View> T.clickWithTrigger(interval: Long = DEFAULT_CLICK_INTERVAL, block: (T) -> Unit) {
    setOnClickListener {
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - triggerLastTime >= interval) {
            triggerLastTime = currentClickTime
            block(this)
        }
    }
}
