package com.app.just_money.my_wallet.model

data class EarningsModel(
    val `data`: Data,
    val force_logout: Int,
    val message: String,
    val status: Int
)