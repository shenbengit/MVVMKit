package com.shencoder.mvvmkit.util

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.*

/**
 *
 * @author  ShenBen
 * @date    2020/11/27 13:51
 * @email   714081644@qq.com
 */
class Nv21ToBitmap constructor(context: Context) {
    private val mRenderScript: RenderScript = RenderScript.create(context)
    private val mScriptIntrinsicYuvToRGB =
        ScriptIntrinsicYuvToRGB.create(mRenderScript, Element.U8_4(mRenderScript))
    private var yuvType: Type.Builder? = null
    private var rgbaType: Type.Builder? = null
    private var inAllocation: Allocation? = null
    private var outAllocation: Allocation? = null

    /**
     * 用于复用
     */
    private var mBitmap: Bitmap? = null

    /**
     * nv21转bitmap
     */
    fun nv21ToBitmap(nv21: ByteArray, width: Int, height: Int): Bitmap {
        if (yuvType == null) {
            yuvType = Type.Builder(mRenderScript, Element.U8(mRenderScript)).setX(nv21.size)
                .apply {
                    inAllocation =
                        Allocation.createTyped(mRenderScript, create(), Allocation.USAGE_SCRIPT)
                }
        }
        if (rgbaType == null) {
            rgbaType =
                Type.Builder(mRenderScript, Element.RGBA_8888(mRenderScript)).setX(width)
                    .setY(height)
                    .apply {
                        outAllocation =
                            Allocation.createTyped(mRenderScript, create(), Allocation.USAGE_SCRIPT)
                    }
        }
        val bitmap = mBitmap ?: let {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            mBitmap = bitmap
            bitmap
        }
        inAllocation?.copyFrom(nv21)
        mScriptIntrinsicYuvToRGB.setInput(inAllocation)
        mScriptIntrinsicYuvToRGB.forEach(outAllocation)
        outAllocation?.copyTo(bitmap)
        return bitmap
    }

    fun recycle() {
        mBitmap?.recycle()
        mBitmap = null
    }
}