package com.hardik.movieapp.cmp

import androidx.compose.ui.window.ComposeUIViewController
import com.hardik.movieapp.cmp.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}