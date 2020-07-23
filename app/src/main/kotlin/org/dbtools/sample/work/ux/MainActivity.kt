package org.dbtools.sample.work.ux

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.dbtools.sample.work.databinding.MainActivityBinding
import org.dbtools.sample.work.work.WorkScheduler
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var workScheduler: WorkScheduler

    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.monitorButton.setOnClickListener { showWorkManagerMonitor() }
        binding.startSimpleButton.setOnClickListener { workScheduler.scheduleSimpleWork("Test") }
        binding.startSyncButton.setOnClickListener { workScheduler.scheduleSync() }
        binding.startPeriodicButton.setOnClickListener { workScheduler.schedulePeriodic() }
    }

    private fun showWorkManagerMonitor() {
        val intent = Intent(this, WorkManagerMonitorActivity::class.java)
        startActivity(intent)
    }
}
