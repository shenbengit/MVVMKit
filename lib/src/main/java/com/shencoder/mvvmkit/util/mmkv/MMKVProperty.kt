package com.shencoder.mvvmkit.util.mmkv

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 *
 * @author Shenben
 * @date 2024/9/27 14:12
 * @description
 * @since
 */

class MMKVProperty<V>(
    private val encode: (key: String, value: V) -> Boolean,
    private val decode: (name: String) -> V
) : ReadWriteProperty<Any?, V> {

    override fun getValue(thisRef: Any?, property: KProperty<*>): V {
        return decode(property.name)
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: V) {
        encode(property.name, value)
    }
}

fun mmkvBool(default: Boolean = false, mmkv: MMKV = MmkvUtils.mmkv) =
    MMKVProperty(
        { name, value -> mmkv.encode(name, value) },
        { name: String -> mmkv.decodeBool(name, default) }
    )

fun mmkvBytes(default: ByteArray? = null, mmkv: MMKV = MmkvUtils.mmkv) =
    MMKVProperty(
        { name, value -> mmkv.encode(name, value) },
        { name: String -> mmkv.decodeBytes(name, default) }
    )

fun mmkvDouble(default: Double = 0.0, mmkv: MMKV = MmkvUtils.mmkv) =
    MMKVProperty(
        { name, value -> mmkv.encode(name, value) },
        { name: String -> mmkv.decodeDouble(name, default) }
    )

fun mmkvFloat(default: Float = 0F, mmkv: MMKV = MmkvUtils.mmkv) =
    MMKVProperty(
        { name, value -> mmkv.encode(name, value) },
        { name: String -> mmkv.decodeFloat(name, default) }
    )

fun mmkvInt(default: Int = 0, mmkv: MMKV = MmkvUtils.mmkv) =
    MMKVProperty(
        { name, value -> mmkv.encode(name, value) },
        { name: String -> mmkv.decodeInt(name, default) }
    )

fun mmkvLong(default: Long = 0L, mmkv: MMKV = MmkvUtils.mmkv) =
    MMKVProperty(
        { name, value -> mmkv.encode(name, value) },
        { name: String -> mmkv.decodeLong(name, default) }
    )

fun mmkvStringSet(default: Set<String> = emptySet(), mmkv: MMKV = MmkvUtils.mmkv) =
    MMKVProperty(
        { name, value -> mmkv.encode(name, value) },
        { name: String -> mmkv.decodeStringSet(name, default) }
    )

inline fun <reified T : Parcelable> mmkvParcelable(default: T? = null) =
    MMKVProperty(
        { name, value -> mmkv.encode(name, value) },
        { name: String -> mmkv.decodeParcelable(name, T::class.java, default) }
    )

fun mmkvString(default: String? = null, mmkv: MMKV = MmkvUtils.mmkv) =
    MMKVProperty(
        { name, value -> mmkv.encode(name, value) },
        { name: String -> mmkv.decodeString(name, default) }
    )

fun <V> MMKVProperty<V>.asLiveData() = object : ReadOnlyProperty<Any?, MutableLiveData<V>> {

    private var cache: MutableLiveData<V>? = null

    override fun getValue(thisRef: Any?, property: KProperty<*>): MutableLiveData<V> =
        cache ?: object : MutableLiveData<V>() {
            override fun getValue() = this@asLiveData.getValue(thisRef, property)

            override fun setValue(value: V) {
                if (super.getValue() == value) return
                super.setValue(value)
                this@asLiveData.setValue(thisRef, property, value)
            }

            override fun onActive() {
                super.setValue(value)
            }
        }.also { cache = it }
}