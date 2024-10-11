package com.dixon.app.kmmsample.android.base

import com.dixon.app.kmmsample.database.DBManager
import com.dixon.app.kmmsample.database.KVStore
import java.lang.ref.WeakReference

class AppApplication : BaseApplication() {

    // 主线程初始化依赖
    override fun doInitDepend() {
        DBManager.contextRef = WeakReference(this)
        KVStore.contextRef = WeakReference(this)
    }

    // 子线程初始化依赖
    override fun doInitDependAsync() {

    }
}