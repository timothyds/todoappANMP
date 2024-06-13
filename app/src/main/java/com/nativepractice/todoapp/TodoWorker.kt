package com.nativepractice.todoapp

import android.app.Activity
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.nativepractice.todoapp.util.NotificationHelper

class TodoWorker(context: Context, params: WorkerParameters): Worker(context,params)  {
    override fun doWork(): Result {
        NotificationHelper(applicationContext).createNotification(
            inputData.getString("title").toString(),
            inputData.getString("message").toString())
        return Result.success()
    }

}