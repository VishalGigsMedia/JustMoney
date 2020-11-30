package com.app.just_money.my_wallet.faq.model


import com.google.gson.annotations.SerializedName

data class FaqData(
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("created_by")
    val createdBy: String?,
    @SerializedName("faq_answer")
    val faqAnswer: String?,
    @SerializedName("faq_id")
    val faqId: String?,
    @SerializedName("faq_question")
    val faqQuestion: String?,
    @SerializedName("faq_status")
    val faqStatus: String?,
    @SerializedName("lang_code")
    val langCode: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    var showMore: Boolean = false,
)