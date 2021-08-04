package com.app.cent4free.my_wallet.faq.model


import com.google.gson.annotations.SerializedName

data class FaqModel(
    @SerializedName("data")
    var faqData: List<FaqData>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)