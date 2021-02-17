package com.app.just_money.my_wallet.faq.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.faq.model.FaqModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FaqRepository {

    private val TAG = javaClass.simpleName
    private var faqModel: FaqModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getFaq(context: Context, api: API): MutableLiveData<FaqModel> {
        val mutableLiveData: MutableLiveData<FaqModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            api.getFaq(preferenceHelper.getJwtToken()).enqueue(object : Callback<FaqModel> {
                override fun onResponse(call: Call<FaqModel>, response: Response<FaqModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    faqModel = gson?.fromJson(json, FaqModel::class.java)
                    println("$TAG : $json")
                    mutableLiveData.value = faqModel
                }

                override fun onFailure(call: Call<FaqModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                }
            })
        } else {
            DefaultHelper.showToast(context, context.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

}