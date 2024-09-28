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
inline val mmkv: MMKV
    get() = MmkvUtils.mmkv

/**
 * 用户级[MMKV]存储
 * 跟随用户生命周期，在用户退出登录时清除存储
 */
object MmkvUtils {

    private const val MMAP_ID = "User_mmkv"

    @JvmField
    val mmkv = MMKV.mmkvWithID(MMAP_ID, libEnvironment.mmkvMode, libEnvironment.mmkvCryptKey)

    @JvmStatic
    fun clear() {
        mmkv.clearAll()
    }
}