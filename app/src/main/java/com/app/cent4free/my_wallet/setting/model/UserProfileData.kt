package com.app.cent4free.my_wallet.setting.model


import com.google.gson.annotations.SerializedName

data class UserProfileData(
    @SerializedName("available_coins")
    val availableCoins: String?,
    @SerializedName("dob")
    val dob: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("email_verification_message")
    val emailVerificationMessage: String?,
    @SerializedName("email_verified_at")
    val emailVerifiedAt: Any?,
    @SerializedName("firstname")
    val firstname: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("lastname")
    val lastname: String?,
    @SerializedName("mobile")
    val mobile: String?,
    @SerializedName("profile_pic")
    val profilePic: Any?
)