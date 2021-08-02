package com.app.just_money.terms_condition

data class TnCModel(
    val `data`: Data,
    val force_logout: Int,
    val message: String,
    val status: Int
)