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
    }
}
