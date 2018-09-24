package org.dbtools.sample.work.work

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler
@Inject constructor(
    private val workManager: WorkManager
) {
    fun scheduleSimpleWork(text: String) {
        val inputData = SimpleWorker.createInputData(text)

        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
            .setConstraints(workerConstraints)
            .setInputData(inputData)
            .build()

        workManager.enqueue(workRequest)
    }

    fun scheduleSync(now: Boolean = false) {
        val workerConstraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequestBuilder = OneTimeWorkRequest.Builder(SyncWorker::class.java)
            .setConstraints(workerConstraints)

        if (!now) {
            workRequestBuilder.setInitialDelay(10, TimeUnit.SECONDS)
        }

        val workRequest = workRequestBuilder.build()

        workManager.beginUniqueWork(SyncWorker.UNIQUE_WORK_NAME, ExistingWorkPolicy.REPLACE, workRequest)
            .enqueue()
    }

    fun schedulePeriodic() {
        val workRequest = createStandardPeriodicWorkRequest<SimplePeriodicWorker>(15, TimeUnit.MINUTES)
        workManager.enqueueUniquePeriodicWork(SimplePeriodicWorker.UNIQUE_PERIODIC_WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
    }

    /**
     * Create a standard PERIODIC request that...
     * - Requires a Network Connection
     * - Requires charging
     */
    private inline fun <reified T: Worker> createStandardPeriodicWorkRequest(repeatInterval: Long, timeUnit: TimeUnit, inputDataBuilder: Data.Builder = Data.Builder()): PeriodicWorkRequest {
        val workerConstraintsBuilder = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)

        val workerConstraints = workerConstraintsBuilder.build()

        val workRequestBuilder = PeriodicWorkRequestBuilder<T>(repeatInterval, timeUnit)
            .addTag("monitored")
            .setConstraints(workerConstraints)
            .setInputData(inputDataBuilder.build())

        return workRequestBuilder.build()
    }
}
