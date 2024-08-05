package com.shencoder.mvvmkit.binding

import com.drake.brv.BindingAdapter


/**
 *
 *
 * @date 2022/11/22 14:28
 * @description
 * @since
 */
data class AddPageData @JvmOverloads constructor(
    val list: List<Any?>?,
    val hasMore: Boolean = true,
    val adapter: BindingAdapter? = null
)
