package org.dbtools.sample.work.ux

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import org.dbtools.sample.work.work.WorkScheduler
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var workScheduler: WorkScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "WorkManager Tools"
                            )
                        },
                    )
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    Column(modifier = Modifier.padding(16.dp, 16.dp)) {
                        Button(onClick = { showWorkManagerMonitor() }) { Text("Work Manager Monitor") }
                        Button(onClick = { workScheduler.scheduleSimpleWork("Test") }) { Text("Start Simple") }
                        Button(onClick = { workScheduler.scheduleSync() }) { Text("Start Sync") }
                        Button(onClick = { workScheduler.schedulePeriodic() }) { Text("Start Periodic") }
                    }
                }
            }
        }
    }

    private fun showWorkManagerMonitor() {
        val intent = Intent(this, WorkManagerMonitorActivity::class.java)
        startActivity(intent)
    }
}
