package com.udacity.project4.locationreminder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.udacity.project4.locationreminder.data.dto.ReminderDTO

@Database(entities = [ReminderDTO::class], version = 1, exportSchema = false)
abstract class RemindersDatabase : RoomDatabase() {
    abstract fun reminderDao(): RemindersDao
}