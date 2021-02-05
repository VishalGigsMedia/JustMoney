package com.app.just_money.offer_details.model


import com.google.gson.annotations.SerializedName

data class OfferDetailsModel(
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val offerDetailsData: OfferDetailsData?,
    @SerializedName("status")
    var status: Int?
)