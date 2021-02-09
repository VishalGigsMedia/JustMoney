package com.app.just_money.in_progress.model


import com.google.gson.annotations.SerializedName

data class PendingList(
    @SerializedName("actual_coins")
    val actualCoins: String?,
    @SerializedName("button_text")
    val buttonText: String?,
    @SerializedName("ch_status")
    val chStatus: String?,
    @SerializedName("download_end_date")
    val downloadEndDate: String?,
    @SerializedName("download_start_date")
    val downloadStartDate: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("is_popular")
    val isPopular: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("offer_coins")
    val offerCoins: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("offer_type")
    val offerType: Any?,
    @SerializedName("original_track_link")
    val originalTrackLink: Any?,
    @SerializedName("points")
    val points: String?,
    @SerializedName("short_description")
    val shortDescription: String?,
    @SerializedName("trackier_offer_id")
    val trackierOfferId: String?,
    @SerializedName("type")
    val type: String?
)