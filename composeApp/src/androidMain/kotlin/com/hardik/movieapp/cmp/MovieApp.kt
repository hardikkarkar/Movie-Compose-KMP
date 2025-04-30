package com.hardik.movieapp.cmp

import android.app.Application
import com.hardik.movieapp.cmp.di.initKoin

class MovieApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
    }
}