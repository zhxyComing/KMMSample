package com.dixon.app.kmmsample

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform