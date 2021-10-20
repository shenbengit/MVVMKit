package com.shencoder.mvvmkit.util;

import android.widget.Toast;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author ShenBen
 * @date 2021/10/20 11:16
 * @email 714081644@qq.com
 */
@IntDef(value = {
        Toast.LENGTH_SHORT,
        Toast.LENGTH_LONG
})
@Retention(RetentionPolicy.SOURCE)
public @interface ToastDuration {
}
