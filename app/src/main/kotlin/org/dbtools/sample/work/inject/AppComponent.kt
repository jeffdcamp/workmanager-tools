package org.dbtools.sample.work.inject

import android.app.Application
import dagger.Component
import org.dbtools.sample.work.App
import org.dbtools.sample.work.ux.MainActivity
import org.dbtools.sample.work.work.SyncWorker
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(application: App)

    // UI
    fun inject(target: MainActivity)

    // Workers
    fun inject(syncWorker: SyncWorker)

    // Exported for child-components.
    fun application(): Application
}
