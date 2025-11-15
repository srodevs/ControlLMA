package com.controllma

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ControlLMAApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}