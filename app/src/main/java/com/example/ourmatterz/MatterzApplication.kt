package com.example.ourmatterz

import android.app.Application
import com.example.ourmatterz.data.AppContainer
import com.example.ourmatterz.data.Container

class MatterzApplication : Application() {
    lateinit var container: Container

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}