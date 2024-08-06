package com.dixon.app.kmmsample.android.base

import android.app.Activity
import android.app.Application
import android.content.Context

/**
 * desc: Help developers use context safely
 */
object ContextAssistant {

    fun activity(): Activity? = BaseApplication.currentActivity.get()

    fun application(): Application = BaseApplication.application
}

fun asContext(action: Context.() -> Unit) {
    BaseApplication.currentActivity.get()?.let {
        action.invoke(it)
    }
}