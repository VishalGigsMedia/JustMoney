package com.app.just_money.available.model


import com.google.gson.annotations.SerializedName

data class AvailableOfferModel(
    @SerializedName("api_loading_time")
    val apiLoadingTime: String?,
    @SerializedName("data")
    val availableOfferData: AvailableOfferData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    var status: Int?,
    @SerializedName("total_coins")
    val totalCoins: String?,
    @SerializedName("withdrawn")
    val withdrawn: String?,
    @SerializedName("completed")
    val completed: String?,


    )