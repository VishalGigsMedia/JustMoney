package com.app.cent4free.my_wallet.payouts.model

data class PayoutModel(
    val `data`: Data?,
    val force_logout: Int,
    val message: String,
    val status: Int
)