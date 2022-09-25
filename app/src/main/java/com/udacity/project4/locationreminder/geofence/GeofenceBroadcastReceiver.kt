package com.udacity.project4.locationreminder.geofence

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.udacity.project4.locationreminder.geofence.GeofenceTransitionsJobIntentService.Companion.enqueueWork

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val TAG = "GeofenceBroadcastReceiver"
    override fun onReceive(context: Context, intent: Intent) {
        //DONE: implement the onReceive method to receive the geofencing events at the background
        val geofenceEvent = GeofencingEvent.fromIntent(intent)
        if (geofenceEvent.hasError()) {
            Log.d(TAG, "Error on receive !")
            return
        }

        when (geofenceEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                enqueueWork(context, intent)
            }
        }
    }
}