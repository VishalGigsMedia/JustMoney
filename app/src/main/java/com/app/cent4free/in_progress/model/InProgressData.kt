package com.app.cent4free.in_progress.model


import com.app.cent4free.available.model.AvailableOffer
import com.google.gson.annotations.SerializedName

data class InProgressData(
    @SerializedName("offers")
    val pendingList: List<PendingList>?,
    @SerializedName("quick_deals")
    val quickDealsList: List<AvailableOffer>?
    // val quickDealsList: List<QuickDealsList>?
)