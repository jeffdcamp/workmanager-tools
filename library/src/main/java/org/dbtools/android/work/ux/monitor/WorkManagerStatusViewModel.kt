package org.dbtools.android.work.ux.monitor

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
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

            // split up the items... because SQLite can only handle so many in a single query
            val workSpecList = mutableListOf<WorkSpec>()
            workIds.chunked(500).forEach { chunk ->
                workSpecList.addAll(workSpecDao.getWorkSpecs(chunk).toList())
            }

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