package com.app.just_money.login.model


import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("android_version")
    val androidVersion: String?,
    @SerializedName("carrier_name")
    val carrierName: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("cpu")
    val cpu: String?,
    @SerializedName("device_id")
    val deviceId: String?,
    @SerializedName("device_type")
    val deviceType: String?,
    @SerializedName("display")
    val display: String?,
    @SerializedName("dob")
    val dob: String?,
    @SerializedName("fcm_key")
    val fcmKey: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("mobile_model")
    val mobileModel: String?,
    @SerializedName("profile_pic")
    val profilePic: String?,
    @SerializedName("push_notification_status")
    val pushNotificationStatus: String?,
    @SerializedName("referral_code")
    val referralCode: String?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("user_mode")
    val userMode: String?,
    @SerializedName("version_code")
    val versionCode: String?,
    @SerializedName("version_name")
    val versionName: String?,
    @SerializedName("webapp_compString_id")
    val webappCompStringId: Boolean?
)