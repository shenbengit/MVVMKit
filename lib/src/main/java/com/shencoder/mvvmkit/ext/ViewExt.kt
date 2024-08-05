package com.shencoder.mvvmkit.ext

import android.view.View
import androidx.core.view.ViewCompat

fun View.isLayoutRTL(): Boolean {
    return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
}

private var triggerLastTime: Long = 0L
fun <T : View> T.clickWithTrigger(delay: Long = 500, block: (T) -> Unit) {
    setOnClickListener {
        val currentClickTime = System.currentTimeMillis()
        if (currentClickTime - triggerLastTime >= delay) {
            triggerLastTime = currentClickTime
            block(this)
        }
    }
}
