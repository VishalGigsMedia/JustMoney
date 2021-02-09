package com.app.just_money.offer_details.model

data class OfferDetailsModel(
    val data: OfferDetailsData?,
    val force_logout: Int,
    val message: String,
    val status: Int
)