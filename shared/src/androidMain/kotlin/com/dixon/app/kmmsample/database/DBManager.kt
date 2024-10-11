@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.dixon.app.kmmsample.database

import com.dixon.app.kmmsample.KmmSampleDatabase
import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.dixon.app.kmmsample.KmmSampleDatabase.Companion.Schema
import java.lang.ref.WeakReference

// 数据库配置3
actual object DBManager {

    // 使用弱引用引用 Context，防止内存泄露
    // 也可以考虑使用自建工具类中的 Application Context
    var contextRef = WeakReference<Context?>(null)

    private var driverRef: SqlDriver? = null
    private var dbRef: KmmSampleDatabase? = null

    private val ready: Boolean
        get() = driverRef != null

    private fun dbSetup(driver: SqlDriver) {
        val db = KmmSampleDatabase(driver)
        driverRef = driver
        dbRef = db
    }

    // clear 可在适当的时间调用，释放内存
    fun dbClear() {
        driverRef?.close()
        dbRef = null
        driverRef = null
    }

    @JvmStatic
    actual fun appDatabase(): KmmSampleDatabase? {
        if (!ready) {
            val ctx = contextRef.get() ?: return null
            // Android 使用 AndroidSqliteDriver
            dbSetup(AndroidSqliteDriver(Schema, ctx, name = DB_NAME))
        }
        return dbRef
    }
}