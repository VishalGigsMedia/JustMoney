package com.app.just_money.available.model


import com.google.gson.annotations.SerializedName

data class AvailableOfferData(
    @SerializedName("quick_deals") var quickDeals: List<AvailableOffer>?,
    @SerializedName("popular") val popular: List<AvailableOffer>?,
    @SerializedName("daily_reward_points") val dailyRewards: String?,
    @SerializedName("flash_offer") val flashOffer: List<FlashOffer>?,
    @SerializedName("popup") val popup: Popup?,
    @SerializedName("reward_remaining_time") val reward_remaining_time: RewardRemainingTime?
    )