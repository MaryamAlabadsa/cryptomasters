package com.mas.cryptomasters.core.pref

import com.mas.cryptomasters.data.response.ProfileObject
import com.mas.cryptomasters.data.response.SettingsObject
import com.mas.cryptomasters.utils.UpdateData

interface PreferencesTask {
    fun setUserProfile(userProfile: ProfileObject)
    fun getUserProfile(): ProfileObject

    fun setAppSettings(settingsObjects: SettingsObject)
    fun getAppSettings(): SettingsObject

    fun clearPreferences()

    fun getLanguage(): String
    fun setLanguage(lang: String)


    fun updateSomeData(updateData: UpdateData)
    fun getSomeUpdate(): UpdateData
    fun clearUpdate()


    fun isGustUser(): Boolean
    fun setGuestStatus(isGust: Boolean)


    fun setFCMToken(token: String)
    fun getFCMToken(): String


    fun incrementAdsCount(reset : Boolean )
    fun getAdsCount(): Int

    fun clearUserData()
}