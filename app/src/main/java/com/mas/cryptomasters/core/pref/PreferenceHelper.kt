package com.mas.cryptomasters.core.pref

import android.content.Context
import android.content.SharedPreferences
import com.google.common.reflect.TypeToken
import com.google.gson.GsonBuilder
import com.karumi.dexter.listener.PermissionRequestErrorListener
import com.mas.cryptomasters.data.models.coin.Coins
import com.mas.cryptomasters.data.response.ProfileObject
import com.mas.cryptomasters.data.response.SettingsObject
import com.mas.cryptomasters.data.response.home.Coin
import com.mas.cryptomasters.data.response.home.PostsData
import com.mas.cryptomasters.data.response.recommendations.RecommendData
import com.mas.cryptomasters.utils.UpdateData
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PreferenceHelper @Inject constructor(@ApplicationContext context: Context) : PreferencesTask {
    private var preferences: SharedPreferences =
        context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)

    private var gGson = GsonBuilder().serializeNulls().create()
    private val userData = "USER_DATA"
    private val appSettings = "SETTINGS"
    private val appLanguage = "SELECTED_LANGUAGE"
    private val dataUpdate = "UPDATE_DATA"
    private val guestUser = "GUEST"
    private val fcmToken = "FCM_TOKEN"
    private val adsCount = "ADS_COUNT"
    private val marketId = "marketId"
    private val high_24h = "high_24h"
    private val low_24h = "low_24h"
    private val POSTS = "posta"
    private val COINS = "COINS"
    private val RULE = "RULE"
    private val Recommend = "Recommend"


    override fun setUserProfile(userProfile: ProfileObject) {
        preferences.edit()?.remove(guestUser)?.apply()
        preferences.edit()?.putString(userData, gGson.toJson(userProfile))?.apply()
    }

    override fun getUserProfile(): ProfileObject =
        gGson.fromJson(preferences.getString(userData, "{}"), ProfileObject::class.java)

    fun setHomeContent(userProfile: List<PostsData>) {
        val jsonString = gGson.toJson(userProfile)
        preferences.edit()?.putString(POSTS, jsonString)?.apply()
    }

    fun getHomePosts(): List<PostsData> {
        val jsonString = preferences.getString(POSTS, null)
        return if (jsonString != null) {
            val typeToken = object : TypeToken<List<PostsData>>() {}.type
            gGson.fromJson(jsonString, typeToken) ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun setCoinsContent(userProfile: List<Coins>) {
        val jsonString = gGson.toJson(userProfile)
        preferences.edit()?.putString(COINS, jsonString)?.apply()
    }

    fun getCoinsContent(): List<Coins> {
        val jsonString = preferences.getString(COINS, null)
        return if (jsonString != null) {
            val typeToken = object : TypeToken<List<Coins>>() {}.type
            gGson.fromJson(jsonString, typeToken) ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun setRuleContent(userProfile: List<Coin>) {
        val jsonString = gGson.toJson(userProfile)
        preferences.edit()?.putString(RULE, jsonString)?.apply()
    }

    fun getRuleContent(): List<Coin> {
        val jsonString = preferences.getString(RULE, null)
        return if (jsonString != null) {
            val typeToken = object : TypeToken<List<Coin>>() {}.type
            gGson.fromJson(jsonString, typeToken) ?: emptyList()
        } else {
            emptyList()
        }
    }
    fun setRecommend(userProfile: List<RecommendData>) {
        val jsonString = gGson.toJson(userProfile)
        preferences.edit()?.putString(Recommend, jsonString)?.apply()
    }

    fun getRecommend(): List<RecommendData> {
        val jsonString = preferences.getString(Recommend, null)
        return if (jsonString != null) {
            val typeToken = object : TypeToken<List<RecommendData>>() {}.type
            gGson.fromJson(jsonString, typeToken) ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun clearHomeData() {
        preferences.edit()?.remove(POSTS)?.apply()
        preferences.edit()?.remove(COINS)?.apply()
        preferences.edit()?.remove(RULE)?.apply()
        preferences.edit()?.remove(Recommend)?.apply()
    }

    override fun clearUserData() {
        preferences.edit()?.putString(userData, "{}")?.apply()
    }

    override fun setAppSettings(settingsObjects: SettingsObject) {
        preferences.edit()?.putString(appSettings, gGson.toJson(settingsObjects))?.apply()
    }

    override fun getAppSettings(): SettingsObject =
        gGson.fromJson(preferences.getString(appSettings, "{}"), SettingsObject::class.java)

    override fun clearPreferences() {
        preferences.edit().clear().apply()
    }

    override fun getLanguage(): String = preferences.getString(appLanguage, "en").toString()

    override fun setLanguage(lang: String) {
        preferences.edit()?.putString(appLanguage, lang)?.apply()
    }


    override fun updateSomeData(updateData: UpdateData) {
        preferences.edit()?.putString(dataUpdate, gGson.toJson(updateData))?.apply()
    }

    override fun getSomeUpdate(): UpdateData =
        gGson.fromJson(preferences.getString(dataUpdate, "{}"), UpdateData::class.java)

    override fun clearUpdate() {
        preferences.edit()?.remove(dataUpdate)?.apply()
    }

    override fun isGustUser(): Boolean = preferences.getBoolean(guestUser, false)

    override fun setGuestStatus(isGust: Boolean) {
        preferences.edit()?.putBoolean(guestUser, isGust)?.apply()
    }

    override fun setFCMToken(token: String) {
        preferences.edit()?.putString(fcmToken, token)?.apply()

    }

    override fun getFCMToken(): String = preferences.getString(fcmToken, "NNNNNN").toString()

    fun getMarketId(): String = preferences.getString(marketId, "bitcoin").toString()
    fun gethigh_24h(): String = preferences.getFloat(high_24h, 0f).toString()
    fun getlow_24h(): String = preferences.getFloat(low_24h, 0f).toString()

    fun setMarketId(marketId: String, high: Double?, low: Double?) {
        val editor = preferences.edit()
        editor.putString("marketId", marketId)
        editor.putFloat("high_24h", high?.toFloat() ?: 0f)
        editor.putFloat("low_24h", low?.toFloat() ?: 0f)
        editor.apply()
    }

    override fun incrementAdsCount(reset: Boolean) {
        if (reset) {
            preferences.edit()?.putInt(adsCount, 1)?.apply()
        } else {
            preferences.edit()?.putInt(adsCount, (getAdsCount() + 1))?.apply()
        }
    }

    override fun getAdsCount(): Int = preferences.getInt(adsCount, 0)


}

