package com.app.just_money.my_wallet.leaderborard.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.leaderborard.model.LeaderBoardModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LeaderBoardRepository {

    private val TAG = javaClass.simpleName
    private var leaderBoardModel: LeaderBoardModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getLeaderBoard(context: Context, api: API, type: String): MutableLiveData<LeaderBoardModel> {
        val mutableLiveData: MutableLiveData<LeaderBoardModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            api.getLeaderBoard(preferenceHelper.getJwtToken())
                .enqueue(object : Callback<LeaderBoardModel> {
                    override fun onResponse(call: Call<LeaderBoardModel>, response: Response<LeaderBoardModel>) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        leaderBoardModel = gson?.fromJson(json, LeaderBoardModel::class.java)
                        println("$TAG : $json")
                        mutableLiveData.value = leaderBoardModel
                    }

                    override fun onFailure(call: Call<LeaderBoardModel>, t: Throwable) {
                        println("TAG : ${t.printStackTrace()}")
                        mutableLiveData.value = null
                    }
                })
        } else {
            DefaultHelper.showToast(context, context.getString(R.string.no_internet))
            mutableLiveData.value = null
        }
        return mutableLiveData
    }

}