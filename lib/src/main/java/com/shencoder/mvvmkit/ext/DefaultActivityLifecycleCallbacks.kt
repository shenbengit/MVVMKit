package com.shencoder.mvvmkit.ext

import android.app.Activity
import android.app.Application
import android.os.Bundle


/**
 *
 *
 * @date 2023/3/20 19:31
 * @description
 * @since
 */
open class DefaultActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onActivityStarted(activity: Activity) {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onActivityResumed(activity: Activity) {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onActivityPaused(activity: Activity) {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onActivityStopped(activity: Activity) {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }

    override fun onActivityDestroyed(activity: Activity) {
        // Intentionally empty, this can be optionally implemented by subclasses.
    }
}