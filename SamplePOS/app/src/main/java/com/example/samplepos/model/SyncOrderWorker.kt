package com.example.samplepos.model

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class SyncOrderWorker(appContext: Context, workerParams: WorkerParameters): Worker(appContext, workerParams) {

    override fun doWork(): Result {

        // Uploader order and order item
        Log.i("SimplePOS", "Hello")

        // Indicate whether the work finished successfully with the Result
        return Result.success()
    }
}
