package com.shencoder.mvvmkit.util

import android.app.Dialog
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty

/**
 *@author Ben
 *@date 2019/3/26 13:55
 *@email 714081644@qq.com
 */

fun Context.toastError(
    msg: String?,
    @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    if (msg.isNullOrBlank()) return
    Toasty.error(applicationContext, msg, duration, withIcon).show()
}

fun Context.toastSuccess(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    if (msg.isNullOrBlank()) return
    Toasty.success(applicationContext, msg, duration, withIcon).show()
}

fun Context.toastInfo(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    if (msg.isNullOrBlank()) return
    Toasty.info(applicationContext, msg, duration, withIcon).show()
}

fun Context.toastWarning(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    if (msg.isNullOrBlank()) return
    Toasty.warning(applicationContext, msg, duration, withIcon).show()
}

fun Context.toastNormal(msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT) {
    if (msg.isNullOrBlank()) return
    Toasty.normal(applicationContext, msg, duration).show()
}

fun Fragment.toastError(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    requireContext().applicationContext.toastError(msg, duration, withIcon)
}

fun Fragment.toastSuccess(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    requireContext().applicationContext.toastSuccess(msg, duration, withIcon)
}

fun Fragment.toastInfo(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    requireContext().applicationContext.toastInfo(msg, duration, withIcon)
}

fun Fragment.toastWarning(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    requireContext().applicationContext.toastWarning(msg, duration, withIcon)
}

fun Fragment.toastNormal(msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT) {
    requireContext().applicationContext.toastNormal(msg, duration)
}

fun Dialog.toastError(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    context.applicationContext.toastError(msg, duration, withIcon)
}

fun Dialog.toastSuccess(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    context.applicationContext.toastSuccess(msg, duration, withIcon)
}

fun Dialog.toastInfo(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    context.applicationContext.toastInfo(msg, duration, withIcon)
}

fun Dialog.toastWarning(
    msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    context.applicationContext.toastWarning(msg, duration, withIcon)
}

fun Dialog.toastNormal(msg: String?, @ToastDuration duration: Int = Toast.LENGTH_SHORT) {
    context.applicationContext.toastNormal(msg, duration)
}
