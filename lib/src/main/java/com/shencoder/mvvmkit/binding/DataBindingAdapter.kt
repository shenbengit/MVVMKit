package com.shencoder.mvvmkit.binding

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.*
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.load
import coil.loadAny
import coil.transform.BlurTransformation
import coil.transform.GrayscaleTransformation
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation


/**
 *@author ShenBen
 *@date 2019/6/21 20:51
 *@email 714081644@qq.com
 */

/**
 * Coil 加载图片资源
 *
 * @param data                  图片资源
 * @param placeholderImageRes   图片加载中显示展位图
 * @param errorImageRes         图片加载失败显示占位图
 * @param fallbackImageRes      当[data]为null时，加载该资源
 * @param roundingRadius        圆角
 * @param isBlur                是否高斯模糊
 * @param isGrayscale           灰度变换
 */
@BindingAdapter(
    value = ["loadImageData", "placeholderImageRes", "errorImageRes", "fallbackImageRes", "roundingRadius", "isBlur", "isGrayscale"],
    requireAll = false
)
fun ImageView.setImageUrl(
    data: Any?,
    placeholderImageRes: Drawable? = null,
    errorImageRes: Drawable? = null,
    fallbackImageRes: Drawable? = null,
    roundingRadius: Float = 0f,
    isBlur: Boolean = false,
    isGrayscale: Boolean = false
) {
    val transformations: MutableList<Transformation> = mutableListOf()
    if (isBlur) {
        transformations.add(BlurTransformation(context))
    }
    if (roundingRadius > 0f) {
        transformations.add(RoundedCornersTransformation(roundingRadius))
    }
    if (isGrayscale) {
        transformations.add(GrayscaleTransformation())
    }
    loadAny(data) {
        placeholder(placeholderImageRes)
        error(errorImageRes)
        fallback(fallbackImageRes)
        transformations(transformations)
    }
}

@BindingAdapter("imageSrc")
fun ImageView.imageSrc(@DrawableRes resId: Int) {
    setImageResource(resId)
}

/**
 * RecyclerView 绑定ItemDecoration
 *
 * @param itemDecoration ItemDecoration
 */
@BindingAdapter(value = ["addRecyclerViewItemDecoration"])
fun RecyclerView.addRecyclerViewItemDecoration(itemDecoration: RecyclerView.ItemDecoration?) {
    if (itemDecoration != null) {
        addItemDecoration(itemDecoration)
    }
}

/**
 * RecyclerView 绑定LayoutManager
 *
 * @param layoutManager  LayoutManager
 */
@BindingAdapter(value = ["setRecyclerViewLayoutManager"])
fun RecyclerView.setRecyclerViewLayoutManager(layoutManager: RecyclerView.LayoutManager?) {
    this.layoutManager = layoutManager
}

/**
 * RecyclerView 绑定Adapter
 * @param adapter        Adapter
 */
@BindingAdapter(value = ["setRecyclerViewAdapter"])
fun RecyclerView.setRecyclerViewAdapter(adapter: RecyclerView.Adapter<out RecyclerView.ViewHolder>?) {
    setAdapter(adapter)
}

@BindingAdapter(value = ["recyclerViewSmoothScrollToPosition"])
fun RecyclerView.recyclerViewSmoothScrollToPosition(position: Int) {
    smoothScrollToPosition(position)
}

@BindingAdapter(value = ["setRecyclerViewItemSupportsChangeAnimations"])
fun RecyclerView.setRecyclerViewItemSupportsChangeAnimations(supportsChangeAnimations: Boolean) {
    if (itemAnimator is DefaultItemAnimator) {
        val defaultItemAnimator: DefaultItemAnimator = itemAnimator as DefaultItemAnimator
        defaultItemAnimator.changeDuration = 0
        defaultItemAnimator.addDuration = 0
        defaultItemAnimator.removeDuration = 0
        defaultItemAnimator.moveDuration = 0
        defaultItemAnimator.supportsChangeAnimations = supportsChangeAnimations
    }
}

@BindingAdapter(value = ["setViewBackgroundResource"])
fun View.setViewBackgroundResource(@DrawableRes resId: Int) {
    setBackgroundResource(resId)
}

@BindingAdapter(value = ["setRefreshingStatus"])
fun SwipeRefreshLayout.setRefreshingStatus(isRefreshing: Boolean) {
    this.isRefreshing = isRefreshing
}
