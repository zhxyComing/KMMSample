package com.dixon.app.kmmsample.database

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.util.fastFirstOrNull
import com.dixon.app.kmmsample.PayRecord
import com.dixon.app.kmmsample.core.base.Logger

/**
 * PayRecord 数据中转站
 * <p>
 * 负责搭建内存数据与SQLite数据的桥梁（对数据库的操作会反应到内存集合中）
 */
object PayRecordDataTransfer {

    private val _records by lazy {
        mutableStateListOf<PayRecord>().apply {
            PayRecordDBManager.queryAll()?.let {
                this.addAll(it)
            }
        }
    }

    // 外部读取 records，其实际类型为 StateList，所以 Compose 也会正常重组
    val records: List<PayRecord> get() = _records

    fun refresh() = PayRecordDBManager.queryAll()?.let {
        _records.clear()
        // 排序列表，按时间戳升序排列
        _records.addAll(it)
    }

    fun insert(timestamp: Long, amount: Long, income: Boolean, desc: String) {
        PayRecordDBManager.insert(timestamp, amount, income, desc)
        refresh()
    }

    fun delete(payRecord: PayRecord) {
        PayRecordDBManager.delete(payRecord.id)
        refresh()
    }

    fun deleteById(id: Long) {
        PayRecordDBManager.delete(id)
        refresh()
    }

    fun update(
        id: Long,
        timestamp: Long? = null,
        amount: Long? = null,
        income: Boolean? = null,
        desc: String? = null
    ) {
        records.fastFirstOrNull { it.id == id }?.let { lastRecord ->
            PayRecordDBManager.update(
                id = id,
                timestamp = timestamp ?: lastRecord.timestamp,
                amount = amount ?: lastRecord.amount,
                income = income ?: lastRecord.income.asBool(),
                desc = desc ?: lastRecord.desc,
            )
            refresh()
        }
    }
}

// 筛选从开始时间到结束时间的所有记录
fun List<PayRecord>.filterByTime(start: Long, end: Long) = filter { it.timestamp in start..end }

/**
 * 尝试获取预期金额
 * <p>
 * 如果尚未设置开始与结束时间，则返回0
 */
fun tryObtainExpectAmount(): Long {
    if (PayBasicDataTransfer.beginData.value == null || PayBasicDataTransfer.targetData.value == null) {
        return 0L
    }
    val (beginTime, beginAmount) = PayBasicDataTransfer.beginData.value!!
    val targetTime = PayBasicDataTransfer.targetData.value!!.timestamp
    val periodTotal =
        PayRecordDataTransfer.records.filterByTime(beginTime, targetTime).sumOf {
            if (it.income.asBool()) it.amount else -it.amount
        }
    return beginAmount + periodTotal
}