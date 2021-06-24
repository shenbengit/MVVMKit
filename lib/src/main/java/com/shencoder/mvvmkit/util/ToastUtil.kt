package com.shencoder.mvvmkit.util

import android.app.Dialog
import android.content.Context
import androidx.fragment.app.Fragment
import es.dmoral.toasty.Toasty

/**
 *@author Ben
 *@date 2019/3/26 13:55
 *@email 714081644@qq.com
 */

fun Context.toastError(msg: String) {
    Toasty.error(applicationContext, msg).show()
}

fun Context.toastSuccess(msg: String) {
    Toasty.success(applicationContext, msg).show()
}

fun Context.toastInfo(msg: String) {
    Toasty.info(applicationContext, msg).show()
}

fun Context.toastWarning(msg: String) {
    Toasty.warning(applicationContext, msg).show()
}

fun Context.toastNormal(msg: String) {
    Toasty.normal(applicationContext, msg).show()
}

fun Fragment.toastError(msg: String) {
    Toasty.error(requireContext().applicationContext, msg).show()
}

fun Fragment.toastSuccess(msg: String) {
    Toasty.success(requireContext().applicationContext, msg).show()
}

fun Fragment.toastInfo(msg: String) {
    Toasty.info(requireContext().applicationContext, msg).show()
}

fun Fragment.toastWarning(msg: String) {
    Toasty.warning(requireContext().applicationContext, msg).show()
}

fun Fragment.toastNormal(msg: String) {
    Toasty.normal(requireContext().applicationContext, msg).show()
}

fun Dialog.toastError(msg: String) {
    Toasty.error(context.applicationContext, msg).show()
}

fun Dialog.toastSuccess(msg: String) {
    Toasty.success(context.applicationContext, msg).show()
}

fun Dialog.toastInfo(msg: String) {
    Toasty.info(context.applicationContext, msg).show()
}

fun Dialog.toastWarning(msg: String) {
    Toasty.warning(context.applicationContext, msg).show()
}

fun Dialog.toastNormal(msg: String) {
    Toasty.normal(context.applicationContext, msg).show()
}



