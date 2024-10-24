@file:JvmName("AndroidExt")

package com.shencoder.mvvmkit.ext

import android.app.Activity
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.Px
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.shencoder.mvvmkit.util.AppManager
import kotlin.math.roundToInt

/**
 * 一些常用方法
 */

fun dp2px(dp: Float): Int {
    return AppManager.context.dp2px(dp)
}

fun sp2px(dp: Float): Int {
    return AppManager.context.sp2px(dp)
}

private fun Context.dp2px(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp,
        resources.displayMetrics
    ).roundToInt()
}

private fun Context.sp2px(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP, dp,
        resources.displayMetrics
    ).roundToInt()
}

fun Context.findActivity(): Activity? {
    var context: Context? = this
    do {
        if (context is Activity) {
            return context
        } else if (context is ContextWrapper && context.baseContext.applicationContext != null) {
            context = context.baseContext
        } else {
            return null
        }
    } while (context != null)
    return null
}

fun Context.findFragmentActivity(): FragmentActivity? {
    var context: Context? = this
    do {
        if (context is FragmentActivity) {
            return context
        } else if (context is ContextWrapper && context.baseContext.applicationContext != null) {
            context = context.baseContext
        } else {
            return null
        }
    } while (context != null)
    return null
}

/**
 * Android system share pictures
 */
fun Context.shareImage(uri: Uri) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "image/*"
    intent.putExtra(Intent.EXTRA_STREAM, uri)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(Intent.createChooser(intent, "shareImage"))
}

/**
 * Android system share text
 */
fun Context.shareText(text: String, subject: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, text)
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(Intent.createChooser(intent, "shareText"))
}

/**
 * Modify the status bar text color
 * tips: only light and dark modes
 */
fun Activity.changeStatusFountColor(isLight: Boolean) {
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
        isLight
}

/**
 * Modify the status bar text color
 * tips: only light and dark modes
 */
fun Dialog.changeStatusFountColor(isLight: Boolean) {
    val window = window ?: return
    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars =
        isLight
}

/**
 * Hidden input method
 * Only applicable to Activity
 */
fun Activity.hideSoftKeyboard() {
    currentFocus?.let { view ->
        val manager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        manager.hideSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    } ?: let {
        WindowCompat.getInsetsController(window, window.decorView)
            .hide(WindowInsetsCompat.Type.ime())
    }
}

/**
 * Hidden input method
 * Only applicable to Fragments
 */
fun Fragment.hideSoftKeyboard() {
    activity?.hideSoftKeyboard()
}

/**
 * Whether the input method is displayed
 */
fun Context.isSoftKeyboardActive(): Boolean {
    val manager =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    return manager.isActive
}

/**
 * 复制文字到剪贴板
 *
 * @param label
 * @param text
 */
fun Context.copyClipData(label: String, text: String) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cm.setPrimaryClip(ClipData.newPlainText(label, text))
}

private val handler = Handler(Looper.getMainLooper())

fun uiPost(runnable: Runnable) {
    handler.post(runnable)
}

fun uiPostDelayed(delay: Long, runnable: Runnable) {
    handler.postDelayed(runnable, delay)
}

/** Returns {@code true} if called on the main thread, {@code false} otherwise. */
fun isOnMainThread() = Looper.getMainLooper() == Looper.myLooper()

/** Returns {@code true} if called on a background thread, {@code false} otherwise. */
fun isOnBackgroundThread() = isOnMainThread().not()

/**
 * 跳转Activity
 *
 * @param T
 * @param block
 * @receiver
 */
inline fun <reified T : Activity> Context.startActivity(block: Intent.() -> Unit = {}) {
    val intent = Intent(this, T::class.java)
    intent.block()
    if (this !is Activity) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    startActivity(intent)
}

/**
 * 跳转Activity
 *
 * @param T
 * @param block
 * @receiver
 */
inline fun <reified T : Activity> Fragment.startActivity(block: Intent.() -> Unit = {}) {
    requireContext().startActivity<T>(block)
}

@ColorInt
fun @receiver:ColorRes Int.getColor() =
    ContextCompat.getColor(AppManager.context, this)

fun @receiver:DrawableRes Int.getDrawable() =
    ContextCompat.getDrawable(AppManager.context, this)

@Px
fun @receiver:DimenRes Int.getDimensionPixelSize() =
    AppManager.context.resources.getDimensionPixelSize(this)
