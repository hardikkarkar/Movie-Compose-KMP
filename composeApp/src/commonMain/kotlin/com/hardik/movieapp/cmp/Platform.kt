package com.hardik.movieapp.cmp

interface Platform {
    val name: String
}

enum class PlatformName {
    ANDROID,
    IOS,
    WEB
}

expect fun getPlatform(): Platform

fun isAndroid() = getPlatform().name == PlatformName.ANDROID.name
fun isIos() = getPlatform().name == PlatformName.IOS.name
fun isWeb() = getPlatform().name == PlatformName.WEB.name