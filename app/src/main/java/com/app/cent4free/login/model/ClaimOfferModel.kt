package com.app.cent4free.login.model


import com.google.gson.annotations.SerializedName

data class ClaimOfferModel(
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)