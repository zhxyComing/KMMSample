package com.dixon.app.kmmsample.android.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

abstract class BaseApplication : Application() {

    companion object {
        lateinit var application: Application
        lateinit var currentActivity: WeakReference<Activity>
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initLifecycle()
        initDepend()
    }

    private fun initDepend() {
        doInitDepend()
        CoroutineScope(Dispatchers.Default).launch {
            doInitDependAsync()
        }
    }

    // 初始化依赖
    abstract fun doInitDepend()

    // 异步初始化依赖 优先级不高的可以放这里
    abstract fun doInitDependAsync()

    private fun initLifecycle() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                currentActivity = WeakReference(activity)
            }

            override fun onActivityStarted(activity: Activity) {

            }

            override fun onActivityResumed(activity: Activity) {
                currentActivity = WeakReference(activity)
            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {

            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }
}