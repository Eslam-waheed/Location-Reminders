package com.udacity.project4.locationreminder.data

import com.udacity.project4.locationreminder.data.dto.ReminderDTO
import com.udacity.project4.locationreminder.data.dto.Result

interface ReminderDataSource {
    suspend fun getReminders(): Result<List<ReminderDTO>>
    suspend fun saveReminder(reminder: ReminderDTO)
    suspend fun getReminder(id: String): Result<ReminderDTO>
    suspend fun deleteAllReminders()
}