package com.mas.cryptomasters

import android.app.Application
import com.mas.cryptomasters.core.pref.PreferenceHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var preferences: PreferenceHelper

    override fun onCreate() {
        super.onCreate()

        // Add your code here to delete data from shared preferences
        deleteSharedPreferenceData()
    }

    private fun deleteSharedPreferenceData() {
        // Access shared preferences and delete the required data
        preferences.clearHomeData()
    }
}

