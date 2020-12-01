package com.app.just_money.login.model


import com.google.gson.annotations.SerializedName

data class ClaimOfferModel(
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)