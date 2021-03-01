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
        AuthorizationKey, UserLogIn, UserId, UserState, UserCity, UserCountry, TotalCoins, Completed, Withdrawn, UserEmail, UserDob, UserGender, UserProfilePic, ReferralCode, UserFirstName, UserLastName, ipAddress
    }

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
        return DefaultHelper.decrypt(this.sharedPreferences.getString(Key.UserId.name, "").toString())
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

    fun setEmail(value: String) {
        this.sharedPreferences.edit().putString(Key.UserEmail.name, value).apply()
    }

    fun getEmail(): String {
        return DefaultHelper.decrypt(this.sharedPreferences.getString(Key.UserEmail.name, "").toString())
    }

    fun setDob(value: String) {
        this.sharedPreferences.edit().putString(Key.UserDob.name, value).apply()
    }

    fun getDob(): String {
        return DefaultHelper.decrypt(this.sharedPreferences.getString(Key.UserDob.name, "").toString())
    }

    fun setGender(value: String) {
        this.sharedPreferences.edit().putString(Key.UserGender.name, value).apply()
    }

    fun getGender(): String {
        return DefaultHelper.decrypt(this.sharedPreferences.getString(Key.UserGender.name, "").toString())
    }

    fun setProfilePic(value: String) {
        this.sharedPreferences.edit().putString(Key.UserProfilePic.name, value).apply()
    }

    fun getProfilePic(): String {
        return DefaultHelper.decrypt(this.sharedPreferences.getString(Key.UserProfilePic.name, "").toString())
    }
    fun setReferralCode(value: String) {
        this.sharedPreferences.edit().putString(Key.ReferralCode.name, value).apply()
    }

    fun getReferralCode(): String {
        return DefaultHelper.decrypt(this.sharedPreferences.getString(Key.ReferralCode.name, "").toString())
    }


    fun setFirstName(value: String) {
        this.sharedPreferences.edit().putString(Key.UserFirstName.name, value).apply()
    }

    fun getFirstName(): String {
        return DefaultHelper.decrypt(this.sharedPreferences.getString(Key.UserFirstName.name, "").toString())
    }

    fun setLastName(value: String) {
        this.sharedPreferences.edit().putString(Key.UserLastName.name, value).apply()
    }

    fun getLastName(): String {
        return DefaultHelper.decrypt(this.sharedPreferences.getString(Key.UserLastName.name, "").toString())
    }
    fun setIpAddress(value: String) {
        this.sharedPreferences.edit().putString(Key.ipAddress.name, value).apply()
    }

    fun getIpAddress(): String {
        return this.sharedPreferences.getString(Key.ipAddress.name, "NA").toString()
    }

    fun setClear() {
        this.sharedPreferences.edit().clear().apply()
    }

    fun setClearSpecific(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}