@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.dixon.app.kmmsample.database

import com.dixon.app.kmmsample.KmmSampleDatabase
import com.dixon.app.kmmsample.PayRecord

const val DB_NAME = "PayRecord.db"  // 数据库实际 db 文件名

// 数据库配置3
expect object DBManager {
    fun appDatabase(): KmmSampleDatabase?
}

/**
 * PayRecordDB 调用接口
 */
object PayRecordDBManager {

    /**
     * 查询所有的 PayRecord 信息
     * <p>
     * 默认时间戳由近及远排序
     */
    fun queryAll(): List<PayRecord>? =
        DBManager.appDatabase()?.payRecordDBQueries?.queryAll()?.executeAsList()

    /**
     * 插入一条新记录
     */
    fun insert(timestamp: Long, amount: Long, income: Boolean, desc: String) =
        DBManager.appDatabase()?.payRecordDBQueries?.insertPayRecord(
            timestamp = timestamp,
            amount = amount,
            income = income.asNumber(),
            desc = desc
        )

    /**
     * 删除一条记录
     */
    fun delete(id: Long) =
        DBManager.appDatabase()?.payRecordDBQueries?.deleteById(id)

    /**
     * 修改一条记录
     */
    fun update(id: Long, timestamp: Long, amount: Long, income: Boolean, desc: String) =
        DBManager.appDatabase()?.payRecordDBQueries?.updateById(
            timestamp = timestamp,
            desc = desc,
            amount = amount,
            income = income.asNumber(),
            id = id
        )
}

fun Boolean.asNumber() = if (this) 1L else 0L

fun Long.asBool() = this != 0L