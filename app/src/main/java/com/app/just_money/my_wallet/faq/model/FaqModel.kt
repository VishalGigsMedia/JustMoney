package com.app.just_money.my_wallet.faq.model


import com.google.gson.annotations.SerializedName

data class FaqModel(
    @SerializedName("data")
    val faqData: List<FaqData>?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: Int?
)