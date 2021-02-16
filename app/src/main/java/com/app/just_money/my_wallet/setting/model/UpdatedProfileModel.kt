package com.app.just_money.my_wallet.setting.model

data class UpdatedProfileModel(val `data`: Data, val forceLogout: Int, val message: String, val status: Int) {
    data class Data(val androidVersion: String, val appVersion: String, val avatar: String, val bToken: String,
        val carrierName: String, val city: String, val countryCode: String, val cpu: String, val createdAt: String,
        val createdBy: String?, val deviceId: String, val deviceType: String, val display: String, val dob: String,
        val email: String, val emailVerificationCode: String, val emailVerificationCodeCreatedAt: String,
        val emailVerified: String, val emailVerifiedAt: String, val fcmKey: String, val firstname: String,
        val forgetpasswordAt: String, val gender: String, val id: String, val inviteCode: String,
        val language: String?, val lastname: String, val mobile: String, val mobileModel: String,
        val profileCompletionReward: String, val profilePic: String, val pushNotificationStatus: String,
        val referralCode: String, val state: String, val status: String, val totalCredit: String?,
        val trackierUserId: String?, val type: String, val updatedAt: String, val userMode: String,
        val versionCode: String, val versionName: String, val webappCompanyId: String)
}