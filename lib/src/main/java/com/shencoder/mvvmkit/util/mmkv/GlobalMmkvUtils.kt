package com.shencoder.mvvmkit.util.mmkv

import com.shencoder.mvvmkit.libEnvironment
import com.tencent.mmkv.MMKV


/**
 *
 * @author Shenben
 * @date 2024/9/26 16:43
 * @description
 * @since
 */

@get:JvmSynthetic
inline val globalMmkv: MMKV
    get() = GlobalMmkvUtils.mmkv

/**
 * 全局[MMKV]存储
 * 跟随app生命周期
 */
object GlobalMmkvUtils {

    private const val MMAP_ID = "Global_mmkv"

    @JvmField
    val mmkv = MMKV.mmkvWithID(MMAP_ID, libEnvironment.mmkvMode, libEnvironment.mmkvCryptKey)

    @JvmStatic
    fun clear() {
        mmkv.clearAll()
    }
}