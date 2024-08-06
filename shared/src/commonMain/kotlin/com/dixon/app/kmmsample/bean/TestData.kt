package com.dixon.app.kmmsample.bean

import kotlinx.serialization.Serializable

/**
 * 测试接口返回的 json 数据
 */
@Serializable
data class DogData(
    val id: String,
    val url: String,
    val width: Int,
    val height: Int
)