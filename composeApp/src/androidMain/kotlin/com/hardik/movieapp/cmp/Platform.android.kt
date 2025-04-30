package com.hardik.movieapp.cmp

class AndroidPlatform : Platform {
    override val name: String = PlatformName.ANDROID.name
}

actual fun getPlatform(): Platform = AndroidPlatform()