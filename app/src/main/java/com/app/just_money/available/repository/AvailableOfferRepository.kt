package com.app.just_money.available.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.available.model.AvailableOfferModel
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.RequestKeyHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AvailableOfferRepository {
    private val TAG = javaClass.simpleName
    private var availableOfferModel: AvailableOfferModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getOffers(
        context: Context,
        api: API
    ): MutableLiveData<AvailableOfferModel> {
        val mutableLiveData: MutableLiveData<AvailableOfferModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.state = preferenceHelper.getUserState()
            requestKeyHelper.city = preferenceHelper.getUserCity()
            requestKeyHelper.display_id = "1234"
            /*println(
                "RequestHelper :" +
                        " ${requestKeyHelper.state} :" +
                        " ${requestKeyHelper.city} " +
                        ": ${requestKeyHelper.display_id}"
            )*/
            api.getOffers(preferenceHelper.getJwtToken(),requestKeyHelper).enqueue(object : Callback<AvailableOfferModel> {
                override fun onResponse(
                    call: Call<AvailableOfferModel>,
                    response: Response<AvailableOfferModel>
                ) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    availableOfferModel = gson?.fromJson(json, AvailableOfferModel::class.java)
                    println("$TAG : $json")
                    mutableLiveData.value = availableOfferModel
                }

                override fun onFailure(call: Call<AvailableOfferModel>, t: Throwable) {
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