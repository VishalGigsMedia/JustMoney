package com.app.just_money.my_wallet.leaderborard.model

data class LeaderBoardModel(
    val curr_user_rank_monthly: CurrUserRankMonthly,
    val curr_user_rank_weekly: CurrUserRankWeekly,
    val `data`: Data,
    val force_logout: Int,
    val message: String,
    val status: Int
)