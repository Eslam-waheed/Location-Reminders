package com.udacity.project4.locationreminder.geofence

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.core.app.JobIntentService
import com.udacity.project4.locationreminder.data.dto.ReminderDTO
import com.udacity.project4.locationreminder.data.dto.Result
import com.udacity.project4.locationreminder.data.local.RemindersLocalRepository
import com.udacity.project4.utils.sendNotification
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.locationreminder.data.ReminderDataSource
import com.udacity.project4.locationreminder.reminderslist.ReminderDataItem
import org.koin.android.ext.android.get

class GeofenceTransitionsJobIntentService : JobIntentService(), CoroutineScope {
    private var coroutineJob: Job = Job()
    private val TAG = "GeofenceService"
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + coroutineJob

    companion object {
        private const val JOB_ID = 573

        fun enqueueWork(context: Context, intent: Intent) {
            enqueueWork(
                context,
                GeofenceTransitionsJobIntentService::class.java, JOB_ID,
                intent
            )
        }

    }

    override fun onHandleWork(intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        val geofenceList: List<Geofence> =
            geofencingEvent.triggeringGeofences
        sendNotification(geofenceList)
    }

    private fun sendNotification(Geofences: List<Geofence>) {

        for (triggeringGeofence in Geofences) {
            val requestId = when {
                Geofences.isNotEmpty() -> {
                    Log.d(TAG, "sendNotification: " + triggeringGeofence.requestId)
                    triggeringGeofence.requestId
                }
                else -> {
                    Log.e(TAG, "No Geofence Trigger Found !")
                    return
                }
            }
            //if (TextUtils.isEmpty(requestId)) return
            //repository = get()
            //val remindersLocalRepository: RemindersLocalRepository by inject()
            val remindersLocalRepository: ReminderDataSource by inject()
            CoroutineScope(coroutineContext).launch(SupervisorJob()) {
                val result = remindersLocalRepository.getReminder(requestId)
                if (result is Result.Success<ReminderDTO>) {
                    val reminderDTO = result.data
                    sendNotification(
                        this@GeofenceTransitionsJobIntentService, ReminderDataItem(
                            reminderDTO.title,
                            reminderDTO.description,
                            reminderDTO.location,
                            reminderDTO.latitude,
                            reminderDTO.longitude,
                            reminderDTO.id
                        )
                    )
                }
            }
        }
    }
}