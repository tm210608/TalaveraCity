package com.talaveracity.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TalaveraCityApplication : Application() {
    companion object {
        lateinit var instance: TalaveraCityApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}




