package com.example.eboraazule

import android.app.Application
import com.example.eboraazule.di.AppContainer

class EboraApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
