package com.app.cent4free.my_wallet.setting.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.cent4free.common_helper.DefaultHelper
import com.app.cent4free.common_helper.DefaultHelper.encrypt
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.RequestKeyHelper
import com.app.cent4free.my_wallet.setting.model.LogoutModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingRepository {
    private var logoutModel: LogoutModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun logout(context: Context?, api: API): MutableLiveData<LogoutModel> {
        val mutableLiveData: MutableLiveData<LogoutModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            api.logout(preferenceHelper.getJwtToken()).enqueue(object : Callback<LogoutModel> {
                override fun onResponse(call: Call<LogoutModel>, response: Response<LogoutModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    logoutModel = gson?.fromJson(json, LogoutModel::class.java)
                    println("error : $json")
                    mutableLiveData.value = logoutModel
                }

                override fun onFailure(call: Call<LogoutModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                    mutableLiveData.value = null
                }
            })
        } else mutableLiveData.value = null
        return mutableLiveData
    }

    fun changePassword(context: Context?, api: API, oldPassword: String, newPassword: String): MutableLiveData<LogoutModel> {
        val mutableLiveData: MutableLiveData<LogoutModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.old_password = encrypt(oldPassword)
            requestKeyHelper.new_password = encrypt(newPassword)
            api.changePassword(preferenceHelper.getJwtToken(),requestKeyHelper).enqueue(object : Callback<LogoutModel> {
                override fun onResponse(call: Call<LogoutModel>, response: Response<LogoutModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    logoutModel = gson?.fromJson(json, LogoutModel::class.java)
                    println("error : $json")
                    mutableLiveData.value = logoutModel
                }

                override fun onFailure(call: Call<LogoutModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                    mutableLiveData.value = null
                }
            })
        } else mutableLiveData.value = null
        return mutableLiveData
    }


}