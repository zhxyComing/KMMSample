package com.dixon.app.kmmsample.core.base

import platform.Foundation.NSLog

/*
    关于在 iosMain 中使用 ios 原生/第三方 api：
    1. 在 Kotlin Multiplatform 项目中，NSLog 以及其他 NS 前缀的函数和类来自于 Kotlin/Native 提供的对 Apple 平台（iOS 和 macOS）的基础库的绑定。
    这些绑定是 Kotlin/Native 标准库的一部分，允许你在 Kotlin 代码中直接使用 Apple 平台的 API。
    2. 通过使用 Kotlin/Native 提供的 CocoaPods 集成，你可以在 Kotlin Multiplatform 项目中使用 iOS 的第三方 SDK。
 */

internal actual fun print(level: Logger.LogLevel, tag: String, message: String) {
    val logMessage = when (level) {
        Logger.LogLevel.DEBUG -> "DEBUG[$tag]: $message"
        Logger.LogLevel.INFO -> "INFO[$tag]: $message"
        Logger.LogLevel.WARNING -> "WARN[$tag]: $message"
        Logger.LogLevel.ERROR -> "ERROR[$tag]: $message"
        Logger.LogLevel.NONE -> "NONE[$tag]: $message"
    }
    NSLog(logMessage)
}