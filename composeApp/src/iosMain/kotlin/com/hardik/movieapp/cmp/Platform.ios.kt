package com.hardik.movieapp.cmp

class IOSPlatform: Platform {
    override val name: String = PlatformName.IOS.name
}

actual fun getPlatform(): Platform = IOSPlatform()