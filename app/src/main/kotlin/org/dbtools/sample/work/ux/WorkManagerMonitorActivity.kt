package org.dbtools.sample.work.ux

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.dbtools.android.work.ux.monitor.WorkManagerStatusScreen

class WorkManagerMonitorActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WorkManagerStatusScreen(onBack = { onBackPressed() })
        }
    }
}
