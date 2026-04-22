package com.ivabagba.eventide_app

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform