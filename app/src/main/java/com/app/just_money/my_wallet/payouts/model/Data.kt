package com.app.just_money.my_wallet.payouts.model

data class Data(
    val payouts: List<Payout>,
    val total_coins: String
)