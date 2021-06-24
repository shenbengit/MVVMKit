package com.shencoder.mvvmkit.base.repository

import android.content.SharedPreferences
import com.tencent.mmkv.MMKV
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


/**
 *
 * @author  ShenBen
 * @date    2021/03/16 17:29
 * @email   714081644@qq.com
 */
open class BaseRepository : IRepository, KoinComponent {
    /**
     * 代替[SharedPreferences]
     */
    protected val mmkv: MMKV by inject()

}