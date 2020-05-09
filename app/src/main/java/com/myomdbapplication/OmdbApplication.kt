package com.myomdbapplication

import android.app.Application
import com.myomdbapplication.db.omdbModule
import com.myomdbapplication.repository.restServiceModule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
class OmdbApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.INFO)
            androidContext(applicationContext)
            modules(restServiceModule, omdbViewModel, omdbModule)
        }
    }
}