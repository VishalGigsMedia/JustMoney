package com.app.just_money.available.model


import com.google.gson.annotations.SerializedName

data class Popup(
    @SerializedName ("id")
    val id: String?,
    @SerializedName ("name")
    val name: String?,
    @SerializedName("points")
    val points: String?,
    @SerializedName("trackier_offer_id")
    val trackier_offer_id: String?,
    @SerializedName("offer_type")
    val offer_type: String?,
    @SerializedName("original_track_link")
    val original_track_link: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("short_description")
    val short_description: String?,
    @SerializedName("download_start_date")
    val download_start_date: String?,
    @SerializedName("end_date_time")
    val download_end_date: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("countries")
    val countries: String?,
    @SerializedName("countries_status")
    val countries_status: String?,
    @SerializedName("is_popular")
    val is_popular: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("time_remaining")
    val time_remaining: String?,
    @SerializedName("actual_points")
    val actual_coins: String?,
    @SerializedName("offer_points")
    val offer_coins: String?
)