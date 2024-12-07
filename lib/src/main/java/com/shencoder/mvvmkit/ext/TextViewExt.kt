package com.shencoder.mvvmkit.ext

import android.widget.TextView

/**
 *
 * @date    2024/12/07 14:03
 */

val TextView.textTrim: CharSequence
    get() = text.trim()

val TextView.textTrimString: String
    get() = textTrim.toString()

val TextView.textString: String
    get() = text.toString()