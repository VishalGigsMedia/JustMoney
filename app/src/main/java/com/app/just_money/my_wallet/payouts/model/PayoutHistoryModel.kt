package com.app.just_money.my_wallet.payouts.model

data class PayoutHistoryModel(val amount: String, val `data`: List<Data>, val forceLogout: Int,
    val message: String, val points: String, val status: Int) {
    data class Data(val appIcon: String, val chPoints: String, val createdDate: String, val createdTime: String,
        val name: String, val status: String)
}