package com.app.cent4free.available.model


import com.google.gson.annotations.SerializedName

data class RewardRemainingTime(
    @SerializedName ("hours")
    val hours: String?,
    @SerializedName ("minutes")
    val minutes: String?,
    @SerializedName("seconds")
    val seconds: String?,
)