package com.shencoder.mvvmkit.ext

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.shencoder.mvvmkit.base.view.IViewDataBinding
import java.lang.reflect.ParameterizedType


/**
 *
 * @author Shenben
 * @date 2024/9/30 9:40
 * @description
 * @since
 */

internal fun <VDB : ViewDataBinding> IViewDataBinding<*>.inflateBinding(
    inflater: LayoutInflater,
    container: ViewGroup? = null,
    attachToRoot: Boolean = false
): VDB {
    return ((javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VDB>).getMethod(
        "inflate",
        LayoutInflater::class.java,
        ViewGroup::class.java,
        Boolean::class.java
    ).invoke(null, inflater, container, attachToRoot) as VDB
}