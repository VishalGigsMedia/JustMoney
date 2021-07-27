package com.app.just_money.my_wallet.completed.model


import com.google.gson.annotations.SerializedName

data class CompletedOfferData(
    @SerializedName("offers") val completedList: List<CompletedList>?,
)