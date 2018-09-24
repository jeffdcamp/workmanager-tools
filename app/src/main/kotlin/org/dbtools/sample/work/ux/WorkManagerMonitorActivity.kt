package org.dbtools.sample.work.ux

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.dbtools.android.work.ux.monitor.WorkManagerStatusFragment
import org.dbtools.sample.work.R


class WorkManagerMonitorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.work_manager_monitor_activity)


        supportFragmentManager.beginTransaction().replace(R.id.fragmentPosition1, WorkManagerStatusFragment()).commit()


//        binding.monitorButton.setOnClickListener { showWorkManagerMonitor() }
//        binding.updateButton.setOnClickListener { updateLastIndividual() }
//        binding.deleteButton.setOnClickListener { deleteLastIndividual() }
//        binding.showButton.setOnClickListener { showLastIndividualName() }
//        binding.testDatabaseRepositoryButton.setOnClickListener { testDatabaseRepository() }
//        binding.testRoomLiveDataButton.setOnClickListener { testRoomLiveData() }
//        binding.testMergeDatabaseButton.setOnClickListener { testMergeDatabase() }
//        binding.logIndividualsButton.setOnClickListener { individualRepository.showAllIndividuals() }
//        binding.deleteAllIndividualsButton.setOnClickListener { individualRepository.deleteAllIndividuals() }
//        binding.validateDatabaseButton.setOnClickListener { testValidateDatabase() }
    }
}
