package com.app.cent4free.available.model


import com.google.gson.annotations.SerializedName

data class VersionModel(
    @SerializedName("force_logout")
    val forceLogout: Int?,
    @SerializedName("is_mandatory")
    val isMandatory: Int?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("version_code")
    val versionCode: Int?
)