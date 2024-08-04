package com.shencoder.mvvmkit.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.Target
import com.elvishew.xlog.XLog

/**
 * 加载图片工具类
 * @since v3.5
 */
object ImageUtils {

    private const val TAG = "ImageUtils"

    //<editor-fold desc="buildRequestManager">
    private fun FragmentActivity.buildRequestManager(): RequestManager? {
        return try {
            Glide.with(this)
        } catch (t: Throwable) {
            XLog.e(
                TAG,
                "Unable to obtain a request manager for a FragmentActivity, error: ${
                    t.stackTraceToString()
                }."
            )
            null
        }
    }

    private fun Fragment.buildRequestManager(): RequestManager? {
        return try {
            Glide.with(this)
        } catch (t: Throwable) {
            XLog.e(
                TAG,
                "Unable to obtain a request manager for a Fragment, error: ${t.stackTraceToString()}."
            )
            null
        }
    }

    private fun Context.buildRequestManager(): RequestManager? {
        return try {
            Glide.with(this)
        } catch (t: Throwable) {
            XLog.e(
                TAG,
                "Unable to obtain a request manager for a Context, error: ${t.stackTraceToString()}."
            )
            null
        }
    }

    /**
     * Glide.with(view) 此步骤由于会优先查找与view绑定的Fragment，再查找Activity，层级复杂的话，可能会低效
     */
    private fun ImageView.buildRequestManager(): RequestManager? {
        return try {
            Glide.with(this)
        } catch (t: Throwable) {
            XLog.e(
                TAG,
                "Unable to obtain a request manager for a ImageView, error: ${t.stackTraceToString()}."
            )
            null
        }
    }
    //</editor-fold>

    //<editor-fold desc="ImageView扩展">
    @JvmStatic
    @JvmOverloads
    fun ImageView.loadImage(obj: Any?, request: RequestBuilder<Drawable>.() -> Unit = {}) {
        val requests = buildRequestManager() ?: return
        requests.load(obj)
            .apply(request)
            .into(this)
    }

    @JvmStatic
    @JvmOverloads
    fun ImageView.loadImageAsBitmap(obj: Any?, request: RequestBuilder<Bitmap>.() -> Unit = {}) {
        val requests = buildRequestManager() ?: return
        requests.asBitmap()
            .load(obj)
            .apply(request)
            .into(this)
    }

    @JvmStatic
    @JvmOverloads
    fun ImageView.loadImageAsGif(obj: Any?, request: RequestBuilder<GifDrawable>.() -> Unit = {}) {
        val requests = buildRequestManager() ?: return
        requests.asGif()
            .load(obj)
            .apply(request)
            .into(this)
    }
    //</editor-fold>

    //<editor-fold desc="FragmentActivity扩展">
    @JvmStatic
    @JvmOverloads
    fun FragmentActivity.loadImage(
        obj: Any?,
        target: Target<Drawable>,
        request: RequestBuilder<Drawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.load(obj)
            .apply(request)
            .into(target)
    }

    @JvmStatic
    @JvmOverloads
    fun FragmentActivity.loadImage(
        obj: Any?,
        view: ImageView,
        request: RequestBuilder<Drawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.load(obj)
            .apply(request)
            .into(view)
    }

    @JvmStatic
    @JvmOverloads
    fun FragmentActivity.loadImageAsBitmap(
        obj: Any?,
        target: Target<Bitmap>,
        request: RequestBuilder<Bitmap>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asBitmap()
            .load(obj)
            .apply(request)
            .into(target)
    }

    @JvmStatic
    @JvmOverloads
    fun FragmentActivity.loadImageAsBitmap(
        obj: Any?,
        view: ImageView,
        request: RequestBuilder<Bitmap>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asBitmap()
            .load(obj)
            .apply(request)
            .into(view)
    }

    @JvmStatic
    @JvmOverloads
    fun FragmentActivity.loadImageAsGif(
        obj: Any?,
        target: Target<GifDrawable>,
        request: RequestBuilder<GifDrawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asGif()
            .load(obj)
            .apply(request)
            .into(target)
    }

    @JvmStatic
    @JvmOverloads
    fun FragmentActivity.loadImageAsGif(
        obj: Any?,
        view: ImageView,
        request: RequestBuilder<GifDrawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asGif()
            .load(obj)
            .apply(request)
            .into(view)
    }
    //</editor-fold>

    //<editor-fold desc="Fragment扩展">
    @JvmStatic
    @JvmOverloads
    fun Fragment.loadImage(
        obj: Any?,
        target: Target<Drawable>,
        request: RequestBuilder<Drawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.load(obj)
            .apply(request)
            .into(target)
    }

    @JvmStatic
    @JvmOverloads
    fun Fragment.loadImage(
        obj: Any?,
        view: ImageView,
        request: RequestBuilder<Drawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.load(obj)
            .apply(request)
            .into(view)
    }

    @JvmStatic
    @JvmOverloads
    fun Fragment.loadImageAsBitmap(
        obj: Any?,
        target: Target<Bitmap>,
        request: RequestBuilder<Bitmap>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asBitmap()
            .load(obj)
            .apply(request)
            .into(target)
    }

    @JvmStatic
    @JvmOverloads
    fun Fragment.loadImageAsBitmap(
        obj: Any?,
        view: ImageView,
        request: RequestBuilder<Bitmap>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asBitmap()
            .load(obj)
            .apply(request)
            .into(view)
    }

    @JvmStatic
    @JvmOverloads
    fun Fragment.loadImageAsGif(
        obj: Any?,
        target: Target<GifDrawable>,
        request: RequestBuilder<GifDrawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asGif()
            .load(obj)
            .apply(request)
            .into(target)
    }

    @JvmStatic
    @JvmOverloads
    fun Fragment.loadImageAsGif(
        obj: Any?,
        view: ImageView,
        request: RequestBuilder<GifDrawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asGif()
            .load(obj)
            .apply(request)
            .into(view)
    }
    //</editor-fold>

    //<editor-fold desc="Context扩展">
    @JvmStatic
    @JvmOverloads
    fun Context.loadImage(
        obj: Any?,
        target: Target<Drawable>,
        request: RequestBuilder<Drawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.load(obj)
            .apply(request)
            .into(target)
    }

    @JvmStatic
    @JvmOverloads
    fun Context.loadImage(
        obj: Any?,
        view: ImageView,
        request: RequestBuilder<Drawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.load(obj)
            .apply(request)
            .into(view)
    }

    @JvmStatic
    @JvmOverloads
    fun Context.loadImageAsBitmap(
        obj: Any?,
        target: Target<Bitmap>,
        request: RequestBuilder<Bitmap>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asBitmap()
            .load(obj)
            .apply(request)
            .into(target)
    }

    @JvmStatic
    @JvmOverloads
    fun Context.loadImageAsBitmap(
        obj: Any?,
        view: ImageView,
        request: RequestBuilder<Bitmap>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asBitmap()
            .load(obj)
            .apply(request)
            .into(view)
    }

    @JvmStatic
    @JvmOverloads
    fun Context.loadImageAsGif(
        obj: Any?,
        target: Target<GifDrawable>,
        request: RequestBuilder<GifDrawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asGif()
            .load(obj)
            .apply(request)
            .into(target)
    }

    @JvmStatic
    @JvmOverloads
    fun Context.loadImageAsGif(
        obj: Any?,
        view: ImageView,
        request: RequestBuilder<GifDrawable>.() -> Unit = {}
    ) {
        val requests = buildRequestManager() ?: return
        requests.asGif()
            .load(obj)
            .apply(request)
            .into(view)
    }
    //</editor-fold>
}