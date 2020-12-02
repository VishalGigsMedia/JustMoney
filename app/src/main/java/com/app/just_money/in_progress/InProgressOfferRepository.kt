package com.app.just_money.in_progress

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.RequestKeyHelper
import com.app.just_money.my_wallet.completed.model.CompletedOfferModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InProgressOfferRepository {
    private val TAG = javaClass.simpleName
    private var completedOfferModel: CompletedOfferModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getInProgressOffers(
        context: Context,
        api: API
    ): MutableLiveData<CompletedOfferModel> {
        val mutableLiveData: MutableLiveData<CompletedOfferModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestHelper = RequestKeyHelper()
            requestHelper.state = preferenceHelper.getUserState()
            api.getInProgressOffers(preferenceHelper.getJwtToken(), requestHelper)
                .enqueue(object : Callback<CompletedOfferModel> {
                    override fun onResponse(
                        call: Call<CompletedOfferModel>,
                        response: Response<CompletedOfferModel>
                    ) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        completedOfferModel = gson?.fromJson(json, CompletedOfferModel::class.java)
                        mutableLiveData.value = completedOfferModel
                    }

                    override fun onFailure(call: Call<CompletedOfferModel>, t: Throwable) {
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