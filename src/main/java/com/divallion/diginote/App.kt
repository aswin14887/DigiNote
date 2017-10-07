package com.divallion.diginote

import android.app.Application
import com.divallion.diginote.dagger.AppComponent
import com.divallion.diginote.dagger.AppModule
import com.divallion.diginote.dagger.DaggerAppComponent

class App : Application() {

    private val appComponent : AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
    }

    fun appComponent() = appComponent

}