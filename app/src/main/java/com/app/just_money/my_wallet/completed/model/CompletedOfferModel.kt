package com.app.just_money.my_wallet.completed.model


import com.google.gson.annotations.SerializedName

data class CompletedOfferModel(
    @SerializedName("data")
    val `data`: CompletedOfferData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)