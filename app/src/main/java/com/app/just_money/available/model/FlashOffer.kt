package com.app.just_money.available.model


import com.google.gson.annotations.SerializedName

data class FlashOffer(
    @SerializedName("end_date_time")
    val downloadEndDate: String?,
    @SerializedName("download_start_date")
    val downloadStartDate: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("trackier_offer_id")
    val trackierOfferId: String?,
    @SerializedName("offer_type")
    val offerType: Any?,
    @SerializedName("original_track_link")
    val originalTrackLink: Any?,
    @SerializedName("points")
    val points: String?,
    @SerializedName("short_description")
    val shortDescription: String?,
    @SerializedName("time_remaining")
    val timeRemaining: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("web_points")
    val webPoints: Any?,
    @SerializedName("actual_points")
    val actualCoins: String?,
    @SerializedName("offer_points")
    val offerCoins: String?
)