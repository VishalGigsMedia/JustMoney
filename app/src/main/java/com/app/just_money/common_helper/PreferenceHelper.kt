package com.app.just_money.common_helper

import android.content.Context
import android.content.SharedPreferences

class PreferenceHelper(context: Context?) {

    private val sharedPreferences: SharedPreferences

    init {
        val preference = DefaultKeyHelper.preferenceName
        this.sharedPreferences = context!!.getSharedPreferences(preference, Context.MODE_PRIVATE)
    }


    enum class Key {
        AuthorizationKey,
        UserLogIn,
        UserId,
        UserState,
        UserCity,
        UserCountry,
        TotalCoins,
        Completed,
        Withdrawn,
    }

    /*fun setString(key: String, value: String) {
        this.sharedPreferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String {
        return this.sharedPreferences.getString(key, "").toString()
    }

    fun setBoolean(key: String, value: Boolean) {
        this.sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return this.sharedPreferences.getBoolean(key, false)
    }




    fun setInt(key: String, value: Int) {
        this.sharedPreferences.edit().putInt(key, value).apply()
    }

    fun getInt(key: String): Int {
        return this.sharedPreferences.getInt(key, 0)
    }

    internal fun setCookies(base_url: String, cookies: List<Cookie>) {
        // Convert cookies to JSON and store them
        val gson = Gson()
        val cookiesString = gson.toJson(cookies)
        this.sharedPreferences.edit().putString(base_url, cookiesString).apply()
    }

    internal fun getCookies(base_url: String): List<Cookie> {
        val gson = Gson()
        val cookiesString = this.sharedPreferences.getString(base_url, null)
        return if (cookiesString != null) {
            Arrays.asList(*gson.fromJson(cookiesString, Array<Cookie>::class.java))
        } else ArrayList()
    }
*/

    //set preference data
    fun setUserLoggedIn(value: Boolean) {
        this.sharedPreferences.edit().putBoolean(Key.UserLogIn.name, value).apply()
    }

    fun isUserLoggedIn(): Boolean {
        return this.sharedPreferences.getBoolean(Key.UserLogIn.name, false)
    }

    fun setJwtToken(value: String) {
        this.sharedPreferences.edit().putString(Key.AuthorizationKey.name, value).apply()
    }

    fun getJwtToken(): String {
        return this.sharedPreferences.getString(Key.AuthorizationKey.name, "").toString()
    }

    fun setUserId(value: String) {
        this.sharedPreferences.edit().putString(Key.UserId.name, value).apply()
    }

    fun getUserId(): String {
        return this.sharedPreferences.getString(Key.UserId.name, "").toString()
    }

    fun setUserState(value: String) {
        this.sharedPreferences.edit().putString(Key.UserState.name, value).apply()
    }

    fun getUserState(): String {
        return this.sharedPreferences.getString(Key.UserState.name, "").toString()
    }

    fun setUserCity(value: String) {
        this.sharedPreferences.edit().putString(Key.UserCity.name, value).apply()
    }

    fun getUserCity(): String {
        return this.sharedPreferences.getString(Key.UserCity.name, "").toString()
    }


    fun setUserCountry(value: String) {
        this.sharedPreferences.edit().putString(Key.UserCountry.name, value).apply()
    }

    fun getUserCountry(): String {
        return this.sharedPreferences.getString(Key.UserCountry.name, "").toString()
    }


    fun setTotalCoins(value: String) {
        this.sharedPreferences.edit().putString(Key.TotalCoins.name, value).apply()
    }

    fun getTotalCoins(): String {
        return this.sharedPreferences.getString(Key.TotalCoins.name, "").toString()
    }

    fun setCompleted(value: String) {
        this.sharedPreferences.edit().putString(Key.Completed.name, value).apply()
    }

    fun getCompleted(): String {
        return this.sharedPreferences.getString(Key.Completed.name, "").toString()
    }

    fun setWithdrawn(value: String) {
        this.sharedPreferences.edit().putString(Key.Withdrawn.name, value).apply()
    }

    fun getWithdrawn(): String {
        return this.sharedPreferences.getString(Key.Withdrawn.name, "").toString()
    }


    fun setClear() {
        this.sharedPreferences.edit().clear().apply()
    }

    fun setClearSpecific(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}