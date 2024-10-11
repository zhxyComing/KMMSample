package com.dixon.app.kmmsample.database

import androidx.compose.runtime.mutableStateOf
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val BEGIN_DATA_JSON = "begin_data_json"
private const val TARGET_DATA_JSON = "target_data_json"

/**
 * Pay 基础数据
 */
object PayBasicDataTransfer {

    val beginData = mutableStateOf<BasicData?>(null)
    val targetData = mutableStateOf<BasicData?>(null)

    init {
        beginData.value = KVStore.database()?.string(BEGIN_DATA_JSON)?.let {
            Json.decodeFromString(BasicData.serializer(), it)
        }
        targetData.value = KVStore.database()?.string(TARGET_DATA_JSON)?.let {
            Json.decodeFromString(BasicData.serializer(), it)
        }
    }

    fun setBeginData(basicData: BasicData) {
        KVStore.database()
            ?.set(BEGIN_DATA_JSON, Json.encodeToString(BasicData.serializer(), basicData))
        beginData.value = basicData
    }

    fun setTargetData(basicData: BasicData) {
        KVStore.database()
            ?.set(TARGET_DATA_JSON, Json.encodeToString(BasicData.serializer(), basicData))
        targetData.value = basicData
    }
}

@Serializable
data class BasicData(
    val timestamp: Long,
    val amount: Long
)