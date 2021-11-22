package com.alex_ia.todo_app


import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ListenableWorker.Result.success
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.alex_ia.todo_app.LocalDateTimeConverter.toDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class NotificationManagerImpl(private val context: Context,params: WorkerParameters): Worker(context,params) {


    fun CreateNotification(task: Task){

        val resultIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("isTaskDetail",true)
            putExtra("task",task)
        }

        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(resultIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context,"TASK_CHANNEL")
            .setSmallIcon(R.drawable.ic_clock)
            .setContentTitle(task.title)
            .setContentText(task.description)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(resultPendingIntent)

        with(NotificationManagerCompat.from(context)){
            notify(task.id,builder.build())
        }

    }

    override fun doWork(): Result {
        val id = inputData.getInt("notificationID",0)
        val title = inputData.getString("notificationTitle") ?: ""
        val description = inputData.getString("notificationDescription") ?: ""
        val date = inputData.getString("notificationDateTime")?: ""

        CreateNotification(Task(id,title,description,toDateTime(date)))

        return success()
    }

}