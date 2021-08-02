package com.app.just_money.terms_condition

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TCRepository {

    private val TAG = javaClass.simpleName
    private var tnCModel: TnCModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getTC(context: Context, api: API): MutableLiveData<TnCModel> {
        val mutableLiveData: MutableLiveData<TnCModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            api.getTC(preferenceHelper.getJwtToken()).enqueue(object : Callback<TnCModel> {
                override fun onResponse(call: Call<TnCModel>, response: Response<TnCModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    tnCModel = gson?.fromJson(json, TnCModel::class.java)
                    println("$TAG : $json")
                    mutableLiveData.value = tnCModel
                }

                override fun onFailure(call: Call<TnCModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                    mutableLiveData.value = null
                }
            })
        } else {
            mutableLiveData.value = null
        }
        return mutableLiveData
    }
    fun getPP(context: Context, api: API): MutableLiveData<TnCModel> {
        val mutableLiveData: MutableLiveData<TnCModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            api.getPP(preferenceHelper.getJwtToken()).enqueue(object : Callback<TnCModel> {
                override fun onResponse(call: Call<TnCModel>, response: Response<TnCModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    tnCModel = gson?.fromJson(json, TnCModel::class.java)
                    println("$TAG : $json")
                    mutableLiveData.value = tnCModel
                }

                override fun onFailure(call: Call<TnCModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                    mutableLiveData.value = null
                }
            })
        } else {
            mutableLiveData.value = null
        }
        return mutableLiveData
    }

}