@file:JvmName("BindingAdapterExt")

package com.shencoder.mvvmkit.ext

import androidx.annotation.IntRange
import com.drake.brv.BindingAdapter

fun BindingAdapter.removeModelAt(index: Int, animation: Boolean = true) {
    mutable.removeAt(index)
    if (animation) {
        notifyItemRemoved(index)
    } else {
        notifyDataSetChanged()
    }
}

fun BindingAdapter.addModel(
    model: Any?,
    animation: Boolean = true,
    @IntRange(from = -1) index: Int = -1
) {
    addModels(listOf(model), animation, index)
}