package com.app.just_money.login.model


import com.google.gson.annotations.SerializedName

data class LoginData(
    @SerializedName("android_version")
    val androidVersion: String?,
    @SerializedName("carrier_name")
    val carrierName: String?,
    @SerializedName("city")
    val city: Any?,
    @SerializedName("cpu")
    val cpu: String?,
    @SerializedName("device_id")
    val deviceId: String?,
    @SerializedName("device_type")
    val deviceType: String?,
    @SerializedName("display")
    val display: String?,
    @SerializedName("dob")
    val dob: Any?,
    @SerializedName("fcm_key")
    val fcmKey: String?,
    @SerializedName("language")
    val language: Any?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("mobile_model")
    val mobileModel: String?,
    @SerializedName("profile_pic")
    val profilePic: Any?,
    @SerializedName("push_notification_status")
    val pushNotificationStatus: Int?,
    @SerializedName("referral_code")
    val referralCode: String?,
    @SerializedName("state")
    val state: Any?,
    @SerializedName("token")
    val token: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("user_mode")
    val userMode: Int?,
    @SerializedName("version_code")
    val versionCode: String?,
    @SerializedName("version_name")
    val versionName: String?,
    @SerializedName("webapp_company_id")
    val webappCompanyId: Boolean?
)