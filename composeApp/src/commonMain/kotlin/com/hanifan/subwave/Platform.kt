package com.hanifan.subwave

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform