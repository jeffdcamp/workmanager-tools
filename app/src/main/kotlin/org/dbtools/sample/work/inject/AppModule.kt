package org.dbtools.sample.work.inject

import android.app.Application
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideWorkManager(application: Application): WorkManager {
        return WorkManager.getInstance(application)
    }
}
