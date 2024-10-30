package com.controllma

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ControlLMAApp : Application() {

    companion object {
        lateinit var mAppContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mAppContext = applicationContext
    }
}