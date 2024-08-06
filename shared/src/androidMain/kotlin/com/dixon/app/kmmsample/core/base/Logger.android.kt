package com.dixon.app.kmmsample.core.base

import android.util.Log

internal actual fun print(level: Logger.LogLevel, tag: String, message: String) {
    when (level) {
        Logger.LogLevel.INFO -> Log.i(tag, message)
        Logger.LogLevel.DEBUG -> Log.d(tag, message)
        Logger.LogLevel.WARNING -> Log.w(tag, message)
        Logger.LogLevel.ERROR -> Log.e(tag, message)
        Logger.LogLevel.NONE -> Log.i(tag, message)
    }
}