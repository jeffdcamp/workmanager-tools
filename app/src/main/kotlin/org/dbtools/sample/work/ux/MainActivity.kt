package org.dbtools.sample.work.ux

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.dbtools.sample.work.R
import org.dbtools.sample.work.databinding.MainActivityBinding
import org.dbtools.sample.work.inject.Injector
import org.dbtools.sample.work.work.WorkScheduler
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var workScheduler: WorkScheduler

    private lateinit var binding: MainActivityBinding

    init {
        Injector.get().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        binding.monitorButton.setOnClickListener { showWorkManagerMonitor() }
        binding.startSimpleButton.setOnClickListener { workScheduler.scheduleSimpleWork("Test") }
        binding.startSyncButton.setOnClickListener { workScheduler.scheduleSync() }
        binding.startPeriodicButton.setOnClickListener { workScheduler.schedulePeriodic() }
//        binding.endSimplePeriodicButton.setOnClickListener { workScheduler.scheduleSimpleWork("Test") }
//        binding.deleteButton.setOnClickListener { deleteLastIndividual() }
//        binding.showButton.setOnClickListener { showLastIndividualName() }
//        binding.testDatabaseRepositoryButton.setOnClickListener { testDatabaseRepository() }
//        binding.testRoomLiveDataButton.setOnClickListener { testRoomLiveData() }
//        binding.testMergeDatabaseButton.setOnClickListener { testMergeDatabase() }
//        binding.logIndividualsButton.setOnClickListener { individualRepository.showAllIndividuals() }
//        binding.deleteAllIndividualsButton.setOnClickListener { individualRepository.deleteAllIndividuals() }
//        binding.validateDatabaseButton.setOnClickListener { testValidateDatabase() }
    }

    private fun showWorkManagerMonitor() {
        val intent = Intent(this, WorkManagerMonitorActivity::class.java)
        startActivity(intent)
    }
}
