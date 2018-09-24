package org.dbtools.android.work.ux.monitor

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.content.Context
import androidx.work.WorkManager
import androidx.work.impl.WorkDatabase
import androidx.work.impl.model.WorkSpec
import java.util.UUID
import kotlin.concurrent.thread

class WorkManagerStatusViewModel : ViewModel() {

    private lateinit var workDatabase: WorkDatabase

    var onWorkSpecListUpdated: (List<WorkSpec>) -> Unit = {}

    @SuppressLint("RestrictedApi")
    fun init(context: Context) {
        workDatabase = WorkDatabase.create(context, false)
    }

    @SuppressLint("RestrictedApi")
    fun getAllWorkSpec() {
        thread {
            val workSpecDao = workDatabase.workSpecDao()
            val workIds = workSpecDao.allWorkSpecIds
            val workSpecList = workSpecDao.getWorkSpecs(workIds).toList()
            onWorkSpecListUpdated(workSpecList)
        }
    }

    fun onCancelWorker(workSpec: WorkSpec): Boolean {
        val workManager = WorkManager.getInstance()
        val workId = UUID.fromString(workSpec.id)
        workManager.cancelWorkById(workId)

        return true
    }

    fun refresh() {
        getAllWorkSpec()
    }
}