package org.dbtools.sample.work.work

import android.content.Context
import android.support.annotation.WorkerThread
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

class SimplePeriodicWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    @WorkerThread
    override fun doWork(): Result {
        val inputText = inputData.getString(KEY_TEXT)

        logProgress("RUNNING PERIODIC: Text: [$inputText]")

        // return result
        return Result.SUCCESS
    }

    private fun logProgress(progress: String) {
        Timber.e("*** SimplePeriodicWorker[$progress] Thread:[${Thread.currentThread().name}]  Job:[${this.id}]")
    }

    companion object {
        const val UNIQUE_PERIODIC_WORK_NAME = "SimplePeriodicWorker"
        private const val KEY_TEXT = "TEXT"

        fun createInputData(
            text: String
        ): Data {
            val dataBuilder = Data.Builder()
                .putString(KEY_TEXT, text)

            return dataBuilder.build()
        }
    }
}
