package com.shencoder.mvvmkit.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.elvishew.xlog.XLog
import com.shencoder.mvvmkit.ext.DefaultActivityLifecycleCallbacks
import java.lang.ref.WeakReference
import java.util.ArrayDeque
import java.util.Deque

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * Application Activity management class: used for Activity management and application exit
 *
 * 支持app前后台监听
 *
 * 仅适应于主进程
 * Adapts only to the main process
 */
object AppManager : DefaultLifecycleObserver {
    private lateinit var applicationContext: Context

    /**
     * 应用是否在前台
     */
    private var _isAppForeground = true
    val isAppForeground: Boolean
        get() = _isAppForeground

    private val processLifecycleObserverList = mutableListOf<OnProcessLifecycleObserver>()

    private val activityDeque: Deque<WeakReference<Activity>> = ArrayDeque()

    @JvmStatic
    fun init(application: Application) {
        this.applicationContext = application.applicationContext

        application.registerActivityLifecycleCallbacks(object :
            DefaultActivityLifecycleCallbacks() {

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                XLog.i("AppManager->onActivityCreated: ${activity::class.java.simpleName}")
                addActivity(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                XLog.i("AppManager->onActivityStarted: ${activity::class.java.simpleName}")
            }

            override fun onActivityResumed(activity: Activity) {
                XLog.i("AppManager->onActivityResumed: ${activity::class.java.simpleName}")
            }

            override fun onActivityPaused(activity: Activity) {
                XLog.i("AppManager->onActivityPaused: ${activity::class.java.simpleName}")
            }

            override fun onActivityStopped(activity: Activity) {
                XLog.i("AppManager->onActivityStopped: ${activity::class.java.simpleName}")
            }

            override fun onActivityDestroyed(activity: Activity) {
                XLog.i("AppManager->onActivityDestroyed: ${activity::class.java.simpleName}")
                removeActivity(activity)
            }
        })

        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @JvmStatic
    val context: Context
        get() = applicationContext

    @JvmStatic
    val activitiesSize: Int
        get() {
            return activityDeque.size
        }

    @JvmStatic
    val activityList: List<Activity>
        get() {
            val list: MutableList<Activity> = ArrayList()
            val iterator = activityDeque.iterator()
            while (iterator.hasNext()) {
                val act = iterator.next().get()
                if (act == null) {
                    iterator.remove()
                } else {
                    list.add(act)
                }
            }
            return list
        }

    /**
     * 添加Activity到堆栈
     * Add the Activity to the stack
     */
    private fun addActivity(activity: Activity) {
        activityDeque.offer(WeakReference(activity))
    }

    /**
     * 结束指定的Activity
     * End the Activity with the specified class name
     */
    private fun removeActivity(activity: Activity) {
        val iterator = activityDeque.descendingIterator()
        while (iterator.hasNext()) {
            val act = iterator.next().get()
            if (act == null || act === activity) {
                iterator.remove()
            }
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     * Get the current Activity (the last one pushed in the stack)
     */
    @JvmStatic
    fun currentActivity(): Activity? {
        val reference = activityDeque.peekLast()
        return reference?.get()
    }

    /**
     * 获取当前栈底的activity
     */
    @JvmStatic
    fun firstActivity(): Activity? {
        val reference = activityDeque.peekFirst()
        return reference?.get()
    }

    @JvmStatic
    fun finishCurrentActivity() {
        val reference = activityDeque.pollLast()
        if (reference != null) {
            val activity = reference.get()
            activity?.finish()
        }
    }


    /**
     * 结束指定类名的Activity
     * End the Activity with the specified class name
     */
    @JvmStatic
    fun finishActivity(cls: Class<*>) {
        val iterator = activityDeque.descendingIterator()
        while (iterator.hasNext()) {
            val act = iterator.next().get()
            if (act == null || act.javaClass == cls) {
                iterator.remove()
                act?.finish()
            }
        }
    }

    /**
     * 结束所有Activity.
     * end all activities
     *
     *
     *
     * 说明：通常情况下程序中正常的Activity都是会自已调用finish方法来结束自已的，
     * 这种情况下就只需要该Activity从本manager中remove掉自已的句柄就可以了。
     * 需要finishAll的场景一般是：程序正常退出了（那么就由本manager来finish所有余下的的未finish掉的activity）、
     * 程序崩溃了（同样由本manager来finish所有余下的的未finish掉的activity）。
     *
     *
     * Explanation: Normally, the normal activities in the program will call the finish method to end themselves.
     * In this case, it is only necessary for the Activity to remove its own handle from the manager.
     * The scenario that requires finishAll is generally: the program exits normally (then the manager will finish all the remaining unfinished activities),
     * The program crashes (it is also up to the manager to finish all the remaining unfinished activities).
     */
    @JvmStatic
    fun finishAllActivity() {
        val iterator = activityDeque.descendingIterator()
        while (iterator.hasNext()) {
            val act = iterator.next().get()
            iterator.remove()
            act?.finish()
        }
    }

    @JvmStatic
    fun addOnProcessLifecycleObserver(observer: OnProcessLifecycleObserver) {
        if (!processLifecycleObserverList.contains(observer)) {
            processLifecycleObserverList.add(observer)
        }
    }

    @JvmStatic
    fun removeOnProcessLifecycleObserver(observer: OnProcessLifecycleObserver) {
        processLifecycleObserverList.remove(observer)
    }

    override fun onStart(owner: LifecycleOwner) {
        XLog.i("AppManager-> onForeground")
        _isAppForeground = true
        processLifecycleObserverList.forEach { it.onForeground() }
    }

    override fun onStop(owner: LifecycleOwner) {
        XLog.i("AppManager-> onBackground")
        _isAppForeground = false
        processLifecycleObserverList.forEach { it.onBackground() }
    }

    interface OnProcessLifecycleObserver {
        fun onForeground() {}
        fun onBackground() {}
    }
}