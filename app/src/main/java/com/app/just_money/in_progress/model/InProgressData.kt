package com.app.just_money.in_progress.model


import com.app.just_money.available.model.AvailableOffer
import com.google.gson.annotations.SerializedName

data class InProgressData(
    @SerializedName("pending")
    val pendingList: List<PendingList>?,
    @SerializedName("quick_deals")
    val quickDealsList: List<AvailableOffer>?
    // val quickDealsList: List<QuickDealsList>?
)