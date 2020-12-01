package com.app.just_money.offer_details.model


import com.google.gson.annotations.SerializedName

data class OfferDetailsData(
    @SerializedName("ch_id")
    val chId: Any?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("note")
    val note: String?,
    @SerializedName("offer_online_offline")
    val offerOnlineOffline: Any?,
    @SerializedName("original_track_link")
    val originalTrackLink: Any?,
    @SerializedName("package_name")
    val packageName: String?,
    @SerializedName("points")
    val points: String?,
    @SerializedName("short_description")
    val shortDescription: String?,
    @SerializedName("steps_to_earn")
    val stepsToEarn: String?,
    @SerializedName("trackier_offer_id")
    val trackierOfferId: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("video_button")
    val videoButton: String?,
    @SerializedName("video_description")
    val videoDescription: String?,
    @SerializedName("video_title")
    val videoTitle: String?,
    @SerializedName("video_url")
    val videoUrl: Any?
)