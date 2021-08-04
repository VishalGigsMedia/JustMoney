package com.app.cent4free.my_wallet.payouts.model

data class Data(
    val payouts: List<Payout>,
    val total_coins: String
)