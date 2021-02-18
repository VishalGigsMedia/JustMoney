package com.app.just_money.my_wallet.payouts

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.payouts.model.PayoutHistoryModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PayoutHistoryRepository {
    private val TAG = javaClass.simpleName
    private var payoutHistoryModel: PayoutHistoryModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getPayoutHistory(context: Context?, api: API): MutableLiveData<PayoutHistoryModel> {
        val mutableLiveData: MutableLiveData<PayoutHistoryModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val authorizationToken = preferenceHelper.getJwtToken()
            //val authorizationToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vNjUuMC4xNTEuMjA2L2Rldi9wdWJsaWMvYXBpL3YyL2xvZ2luMiIsImlhdCI6MTYxMzM5MTUyOSwiZXhwIjoxNjEzOTk2MzI5LCJuYmYiOjE2MTMzOTE1MjksImp0aSI6IkM4WFhSM2lMQ3VSWGVrT0kiLCJzdWIiOjMxLCJwcnYiOiI4N2UwYWYxZWY5ZmQxNTgxMmZkZWM5NzE1M2ExNGUwYjA0NzU0NmFhIn0.OREMq9tang1baolM6Tmg9H9LmEkDecyYRYWEFMnZvTA"
            api.getPayoutHistory(authorizationToken).enqueue(object : Callback<PayoutHistoryModel> {
                override fun onResponse(call: Call<PayoutHistoryModel>, response: Response<PayoutHistoryModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    payoutHistoryModel = gson?.fromJson(json, PayoutHistoryModel::class.java)
                    mutableLiveData.value = payoutHistoryModel
                }

                override fun onFailure(call: Call<PayoutHistoryModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                    mutableLiveData.value = null
                }
            })
        } else {
            DefaultHelper.showToast(context, context?.getString(R.string.no_internet))
            mutableLiveData.value = null
        }
        return mutableLiveData
    }

}