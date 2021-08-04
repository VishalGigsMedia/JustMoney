package com.app.cent4free.my_wallet.leaderborard

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.dagger.API
import com.app.cent4free.my_wallet.leaderborard.model.LeaderBoardModel
import com.app.cent4free.my_wallet.leaderborard.repository.LeaderBoardRepository

class LeaderBoardViewModel : ViewModel() {

    private val leaderRepository: LeaderBoardRepository = LeaderBoardRepository()

    fun getLeaderBoard(context: Context, api: API, type: String): LiveData<LeaderBoardModel> {
        return leaderRepository.getLeaderBoard(context, api, type)
    }


}