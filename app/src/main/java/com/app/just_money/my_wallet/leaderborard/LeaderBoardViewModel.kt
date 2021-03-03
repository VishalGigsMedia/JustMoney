package com.app.just_money.my_wallet.leaderborard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.leaderborard.model.LeaderBoardModel
import com.app.just_money.my_wallet.leaderborard.repository.LeaderBoardRepository

class LeaderBoardViewModel : ViewModel() {

    private val leaderRepository: LeaderBoardRepository = LeaderBoardRepository()

    fun getLeaderBoard(context: Context, api: API, type: String): LiveData<LeaderBoardModel> {
        return leaderRepository.getLeaderBoard(context, api, type)
    }


}