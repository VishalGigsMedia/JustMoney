package com.app.cent4free.my_wallet.payouts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper
import com.app.cent4free.common_helper.DefaultHelper.encrypt
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.RequestKeyHelper
import com.app.cent4free.my_wallet.payouts.model.PayoutModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayoutHistoryRepository {

    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getPayoutHistory(context: Context?, api: API, offset: Int, nextLimit: Int): MutableLiveData<PayoutModel> {
        val mutableLiveData: MutableLiveData<PayoutModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val authorizationToken = preferenceHelper.getJwtToken()
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.scroll_limit = encrypt(nextLimit.toString())
            requestKeyHelper.scroll_offset = encrypt(offset.toString())
            api.getPayoutHistory(authorizationToken,requestKeyHelper).enqueue(object : Callback<PayoutModel> {
                override fun onResponse(call: Call<PayoutModel>, response: Response<PayoutModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    val payoutModel = gson?.fromJson(json, PayoutModel::class.java)
                    mutableLiveData.value = payoutModel
                }

                override fun onFailure(call: Call<PayoutModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                    mutableLiveData.value = null
                }
            })
        } else {
            showToast(context, context?.getString(R.string.no_internet))
            mutableLiveData.value = null
        }
        return mutableLiveData
    }

}