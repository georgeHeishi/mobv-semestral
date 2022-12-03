package com.example.semestralmobv

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.semestralmobv.data.api.ApiService
import com.example.semestralmobv.data.api.models.CheckInPubArgs

class CheckoutWorker(private val appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            1, createNotification()
        )
    }

    override suspend fun doWork(): Result {
        val response =
            ApiService.create(appContext).checkInPub(CheckInPubArgs("", "", "", 0.0, 0.0)).execute()
        return if (response.isSuccessful) Result.success() else Result.failure()
    }

    private fun createNotification(): Notification {
        val builder =
            NotificationCompat.Builder(appContext, "semestralmobv").apply {
                setContentTitle("Smestral MOBV")
                setContentText("Exiting pub ...")
                setSmallIcon(R.drawable.pin)
                priority = NotificationCompat.PRIORITY_DEFAULT
            }

        return builder.build()
    }


}