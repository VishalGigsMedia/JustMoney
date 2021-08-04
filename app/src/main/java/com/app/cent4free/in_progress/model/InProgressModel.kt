package com.app.cent4free.in_progress.model


import com.google.gson.annotations.SerializedName

data class InProgressModel(
    @SerializedName("data")
    val `data`: InProgressData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)