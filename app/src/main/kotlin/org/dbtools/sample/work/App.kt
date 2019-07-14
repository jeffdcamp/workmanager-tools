package org.dbtools.sample.work

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.dbtools.sample.work.inject.Injector
import timber.log.Timber


class App : Application() {

    init {
        Injector.init(this)
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        setupLogging()
    }

    private fun setupLogging() {
        Timber.plant(Timber.DebugTree())
    }
}
