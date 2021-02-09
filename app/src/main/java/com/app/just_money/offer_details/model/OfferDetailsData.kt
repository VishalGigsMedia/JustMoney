package com.app.just_money.offer_details.model


data class OfferDetailsData(
    val actual_coins: String,
    val ch_id: String,
    val description: String,
    val id: String,
    val image: String,
    val name: String,
    val note: String,
    val offer_coins: String,
    val offer_online_offline: String,
    val original_track_link: String,
    val package_name: String,
    val points: String,
    val short_description: String,
    val steps_description: List<String>,
    val steps_to_earn: String,
    val trackier_offer_id: String,
    val type: String,
    val url: String,
    val video_button: String,
    val video_description: String,
    val video_title: String,
    val video_url: String
)