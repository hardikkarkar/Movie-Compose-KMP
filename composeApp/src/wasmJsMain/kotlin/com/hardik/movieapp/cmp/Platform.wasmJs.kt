package com.hardik.movieapp.cmp

class WasmPlatform: Platform {
    override val name: String = PlatformName.WEB.name
}

actual fun getPlatform(): Platform = WasmPlatform()