package com.app.just_money.my_wallet.setting.model


import com.google.gson.annotations.SerializedName

data class GetUserProfileModel(
    @SerializedName("force_logout")
    val forceLogout: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("data")
    val userProfileData: UserProfileData?
)