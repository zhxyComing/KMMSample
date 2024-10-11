@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.dixon.app.kmmsample.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.dixon.app.kmmsample.KmmSampleDatabase
import com.dixon.app.kmmsample.KmmSampleDatabase.Companion.Schema
import kotlin.concurrent.AtomicReference
import kotlin.native.concurrent.freeze

// 数据库配置3
actual object DBManager {

    // 考虑到 iOS 并发的特殊性，为保证多线程共享正确，这里需要使用到 AtomicReference
    private val driverRef = AtomicReference<SqlDriver?>(null)
    private val dbRef = AtomicReference<KmmSampleDatabase?>(null)

    private fun dbSetup(driver: SqlDriver) {
        val db = KmmSampleDatabase(driver)
        // 初始化后，即刻 freeze
        driverRef.value = driver.freeze()
        dbRef.value = db.freeze()
    }

    fun dbClear() {
        driverRef.value?.close()
        dbRef.value = null
        driverRef.value = null
    }

    // OC、Swift 调用该方法进行初始化
    fun defaultDriver() {
        dbSetup(NativeSqliteDriver(Schema, DB_NAME))
    }

    actual fun appDatabase(): KmmSampleDatabase? {
        return dbRef.value
    }
}