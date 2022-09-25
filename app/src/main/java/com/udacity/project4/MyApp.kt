package com.udacity.project4

import android.app.Application
import com.udacity.project4.locationreminder.data.ReminderDataSource
import com.udacity.project4.locationreminder.data.local.LocalDB
import com.udacity.project4.locationreminder.data.local.RemindersLocalRepository
import com.udacity.project4.locationreminder.reminderslist.RemindersListViewModel
import com.udacity.project4.locationreminder.savereminder.SaveReminderViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val myModule = module {
            viewModel {
                RemindersListViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
            single {
                SaveReminderViewModel(
                    get(),
                    get() as ReminderDataSource
                )
            }
            single { RemindersLocalRepository(get()) as ReminderDataSource }
            single { LocalDB.createRemindersDao(this@MyApp) }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
    }
}