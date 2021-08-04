package com.app.cent4free.my_wallet.completed.model


import com.google.gson.annotations.SerializedName

data class CompletedOfferData(
    @SerializedName("offers") val completedList: List<CompletedList>?,
)