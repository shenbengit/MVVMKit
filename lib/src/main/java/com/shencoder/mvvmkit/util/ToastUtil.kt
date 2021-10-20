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
    msg: String,
    @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.error(applicationContext, msg, duration, withIcon).show()
}

fun Context.toastSuccess(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.success(applicationContext, msg, duration, withIcon).show()
}

fun Context.toastInfo(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.info(applicationContext, msg, duration, withIcon).show()
}

fun Context.toastWarning(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.warning(applicationContext, msg, duration, withIcon).show()
}

fun Context.toastNormal(msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT) {
    Toasty.normal(applicationContext, msg, duration).show()
}

fun Fragment.toastError(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.error(requireContext().applicationContext, msg, duration, withIcon).show()
}

fun Fragment.toastSuccess(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.success(requireContext().applicationContext, msg, duration, withIcon).show()
}

fun Fragment.toastInfo(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.info(requireContext().applicationContext, msg, duration, withIcon).show()
}

fun Fragment.toastWarning(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.warning(requireContext().applicationContext, msg, duration, withIcon).show()
}

fun Fragment.toastNormal(msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT) {
    Toasty.normal(requireContext().applicationContext, msg, duration).show()
}

fun Dialog.toastError(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.error(context.applicationContext, msg, duration, withIcon).show()
}

fun Dialog.toastSuccess(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.success(context.applicationContext, msg, duration, withIcon).show()
}

fun Dialog.toastInfo(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.info(context.applicationContext, msg, duration, withIcon).show()
}

fun Dialog.toastWarning(
    msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT,
    withIcon: Boolean = true
) {
    Toasty.warning(context.applicationContext, msg, duration, withIcon).show()
}

fun Dialog.toastNormal(msg: String, @ToastDuration duration: Int = Toast.LENGTH_SHORT) {
    Toasty.normal(context.applicationContext, msg, duration).show()
}
