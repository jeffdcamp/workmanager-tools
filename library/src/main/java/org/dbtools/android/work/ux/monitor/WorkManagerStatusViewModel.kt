package org.dbtools.android.work.ux.monitor

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Configuration
import androidx.work.NetworkType
import androidx.work.WorkManager
import androidx.work.impl.WorkDatabase
import androidx.work.impl.model.WorkSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.concurrent.TimeUnit

@SuppressLint("RestrictedApi")
class WorkManagerStatusViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val workDatabase: WorkDatabase

    private val _workSpecListFlow = MutableStateFlow<List<ListItemData>?>(null)
    var workSpecListFlow: StateFlow<List<ListItemData>?> = _workSpecListFlow.asStateFlow()

    init {
        val configuration: Configuration = Configuration.Builder().build()
        workDatabase = WorkDatabase.create(application, configuration.taskExecutor, false)
        refresh()
    }

    fun refresh() = viewModelScope.launch(Dispatchers.IO) {
        val application = getApplication<Application>()

        val workSpecDao = workDatabase.workSpecDao()
        val workIds = workSpecDao.getAllWorkSpecIds()

        // split up the items... because SQLite can only handle so many in a single query
        val workSpecList = mutableListOf<WorkSpec>()
        workIds.forEach { workSpecId ->
            workSpecDao.getWorkSpec(workSpecId)?.let { workSpecList.add(it) }
        }

        _workSpecListFlow.value = workSpecList.map { workSpec ->
            val name = formatName(workSpec)

            val info = if (workSpec.isPeriodic) {
                formatPeriodic(application, workSpec)
            } else {
                formatOneTime(application, workSpec)
            }

            ListItemData(workSpec.id, name, info)
        }
    }

    fun onCancelWorker(workSpecId: String) = viewModelScope.launch(Dispatchers.IO) {
        val application = getApplication<Application>()
        val workManager = WorkManager.getInstance(application)
        val workId = UUID.fromString(workSpecId)
        workManager.cancelWorkById(workId)
    }

    fun onPrune() = viewModelScope.launch(Dispatchers.IO) {
        val application = getApplication<Application>()
        WorkManager.getInstance(application).pruneWork()
        refresh()
    }

    fun cancelAll() = viewModelScope.launch(Dispatchers.IO) {
        val application = getApplication<Application>()
        WorkManager.getInstance(application).cancelAllWork()
        refresh()
    }

    private fun formatName(workSpec: WorkSpec): String {
        return workSpec.workerClassName.substringAfterLast(".")
    }

    private fun formatOneTime(context: Context, workSpec: WorkSpec): String {
        val schedule = if (workSpec.scheduleRequestedAt > 0) formatDateTime(context, workSpec.scheduleRequestedAt) else ""
        val nextRun = formatDateTime(context, workSpec.calculateNextRunTime())
        return """
            Type: OneTime
            State: ${workSpec.state}
            Constraints: ${formatConstraints(workSpec)}
            Schedule Requested At: $schedule
            Calc Next Run: $nextRun
        """.trimIndent()
    }

    private fun formatPeriodic(context: Context, workSpec: WorkSpec): String {
        val interval = formatInterval(workSpec.intervalDuration)
        val schedule = if (workSpec.scheduleRequestedAt > 0) formatDateTime(context, workSpec.scheduleRequestedAt) else ""
        val nextRun = formatDateTime(context, workSpec.calculateNextRunTime())
        return """
            Type: Periodic
            State: ${workSpec.state}
            Constraints: ${formatConstraints(workSpec)}
            Interval: $interval
            Schedule Requested At: $schedule
            Calc Next Run: $nextRun
        """.trimIndent()

    }

    private fun formatConstraints(workSpec: WorkSpec): String {
        var constraintsText = ""
        if (workSpec.constraints.requiredNetworkType != NetworkType.NOT_REQUIRED) {
            constraintsText += "NETWORK[${workSpec.constraints.requiredNetworkType}] "
        }
        if (workSpec.constraints.requiresBatteryNotLow()) {
            constraintsText += "BATTERY_NOT_LOW "
        }
        if (workSpec.constraints.requiresCharging()) {
            constraintsText += "CHARGING "
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && workSpec.constraints.requiresDeviceIdle()) {
            constraintsText += "IDLE "
        }
        if (workSpec.constraints.requiresStorageNotLow()) {
            constraintsText += "STORAGE_NOT_LOW "
        }
        if (constraintsText.isBlank()) {
            constraintsText += "NONE"
        }

        return constraintsText
    }

    private fun formatDateTime(context: Context, ts: Long): String {
        return DateUtils.formatDateTime(context, ts, (DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME))
    }

    private fun formatInterval(length: Long): String {
        val day = TimeUnit.MILLISECONDS.toDays(length)
        val hr = TimeUnit.MILLISECONDS.toHours(length - TimeUnit.DAYS.toMillis(day))
        val min = TimeUnit.MILLISECONDS.toMinutes(length - TimeUnit.DAYS.toMillis(day) - TimeUnit.HOURS.toMillis(hr))

        return when {
            day > 0 -> String.format("%dd %dh %dm", day, hr, min)
            hr > 0 -> String.format("%dh %dm", hr, min)
            min > 0 -> String.format("%dm", min)
            else -> "$length ms"
        }
    }
}

data class ListItemData(val workSpecId: String, val title: String, val content: String)