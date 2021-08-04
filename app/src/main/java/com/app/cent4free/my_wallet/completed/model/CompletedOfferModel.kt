package com.app.cent4free.my_wallet.completed.model


import com.google.gson.annotations.SerializedName

data class CompletedOfferModel(
    @SerializedName("data")
    val `data`: CompletedOfferData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)