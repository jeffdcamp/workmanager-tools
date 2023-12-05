package org.dbtools.sample.work.work

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters

/**
 * Example simple worker... one that should execute every time it is called
 */
class SimpleWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val inputText = inputData.getString(KEY_TEXT)

        logProgress("RUNNING: Text: [$inputText]")
        try {
            for (i in 1..10) {
                logProgress("RUNNING: Progress: [$i]")
                Thread.sleep(1000)

            }
        } catch (e: InterruptedException) {
            Log.e("SimpleWorker", "Sleep Failure")
        }

        logProgress("FINISHED: Text: [$inputText]")

        // return result
        return Result.success()
    }

    private fun logProgress(progress: String) {
        Log.e("SimpleWorker", "*** SyncWorker[$progress] Thread:[${Thread.currentThread().name}]  Job:[${this.id}]")
    }

    companion object {
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
