package com.example.test

import android.app.Application
import com.example.test.di.apiModule
import com.example.test.di.viewModels
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class UserApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@UserApp)
            modules(listOf(viewModels, apiModule))
        }
    }
}