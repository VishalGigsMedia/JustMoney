package com.app.just_money.my_wallet.setting.model


import com.google.gson.annotations.SerializedName

data class SendFeedbackModel(
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)