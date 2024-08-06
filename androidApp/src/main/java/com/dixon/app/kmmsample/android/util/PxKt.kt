package com.dixon.app.kmmsample.android.util

import android.content.Context

// dp 转 px
fun Float.dp2px(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

// px 转 dp
fun Float.px2dp(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}

// dp 转 px
fun Int.dp2px(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

// px 转 dp
fun Int.px2dp(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}
