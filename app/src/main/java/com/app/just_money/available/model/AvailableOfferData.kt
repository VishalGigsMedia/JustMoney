package com.app.just_money.available.model


import com.google.gson.annotations.SerializedName

data class AvailableOfferData(
    @SerializedName("quick_deals")
    val quickDeals: List<AvailableOffer>?,
    @SerializedName("popular")
    val popular: List<AvailableOffer>?,
    @SerializedName("daily_rewards")
    val dailyRewards: String?,
    @SerializedName("flash_offer")
    val flashOffer: List<FlashOffer>?
)