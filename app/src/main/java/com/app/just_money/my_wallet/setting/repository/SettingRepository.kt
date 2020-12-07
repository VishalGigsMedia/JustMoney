package com.app.just_money.my_wallet.setting.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.setting.model.LogoutModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingRepository {
    private val TAG = javaClass.simpleName
    private var logoutModel: LogoutModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun logout(
        context: Context,
        api: API
    ): MutableLiveData<LogoutModel> {
        val mutableLiveData: MutableLiveData<LogoutModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            api.logout(preferenceHelper.getJwtToken())
                .enqueue(object : Callback<LogoutModel> {
                    override fun onResponse(
                        call: Call<LogoutModel>,
                        response: Response<LogoutModel>
                    ) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        logoutModel = gson?.fromJson(json, LogoutModel::class.java)
                        println("$TAG : $json")
                        mutableLiveData.value = logoutModel
                    }

                    override fun onFailure(call: Call<LogoutModel>, t: Throwable) {
                        println("TAG : ${t.printStackTrace()}")
                    }
                })
        } else {
            DefaultHelper.showToast(
                context,
                context.getString(R.string.no_internet)
            )
        }
        return mutableLiveData
    }


}