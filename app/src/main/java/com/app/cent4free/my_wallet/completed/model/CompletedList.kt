package com.app.cent4free.my_wallet.completed.model

import com.google.gson.annotations.SerializedName

data class CompletedList(
    @SerializedName("actual_points")
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
    @SerializedName("offer_points")
    val offerCoins: String?,
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
    val type: String?,
    @SerializedName("created_at")
    val created_at: String?
)
