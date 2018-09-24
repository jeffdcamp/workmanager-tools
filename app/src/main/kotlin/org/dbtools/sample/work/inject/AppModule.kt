package org.dbtools.sample.work.inject

import android.app.Application
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    internal fun provideApplication(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideWorkManager(): WorkManager {
        return WorkManager.getInstance()
    }
}
