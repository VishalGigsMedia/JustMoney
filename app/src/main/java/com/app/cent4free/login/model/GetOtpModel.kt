package com.app.cent4free.login.model


import com.google.gson.annotations.SerializedName

data class GetOtpModel(
    @SerializedName("message")
    val message: String?,
    @SerializedName("sms_status")
    val smsStatus: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("otp")
    val otp: String?
)