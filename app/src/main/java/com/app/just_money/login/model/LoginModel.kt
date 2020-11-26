package com.app.just_money.login.model


import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("data")
    val `data`: LoginData?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)