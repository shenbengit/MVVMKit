package com.shencoder.mvvmkit.util.moshi

import java.util.Collections
import kotlin.reflect.KType

/**
 *
 *
 * @date    2023/08/01 22:05
 *
 */

interface DefaultValueProvider {
    fun provideDefaultValue(kType: KType): Any?
}

internal class BuildInDefaultValueProvider : DefaultValueProvider {
    override fun provideDefaultValue(kType: KType): Any? {
        return when (kType.classifier) {
            Int::class -> 0
            Char::class -> 0.toChar()
            Byte::class -> 0.toByte()
            Float::class -> 0.toFloat()
            Double::class -> 0.toDouble()
            Long::class -> 0.toLong()
            String::class -> ""
            Boolean::class -> false
            List::class -> Collections.EMPTY_LIST
            Map::class -> Collections.EMPTY_MAP
            Set::class -> Collections.EMPTY_SET
            else -> null
        }
    }
}