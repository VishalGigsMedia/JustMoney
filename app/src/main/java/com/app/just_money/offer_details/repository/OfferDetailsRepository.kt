package com.app.just_money.offer_details.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.RequestKeyHelper
import com.app.just_money.offer_details.model.OfferDetailsModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfferDetailsRepository {

    private val TAG = javaClass.simpleName
    private var offerDetailsModel: OfferDetailsModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getOfferDetails(
        context: Context,
        api: API,
        offerId: String
    ): MutableLiveData<OfferDetailsModel> {
        val mutableLiveData: MutableLiveData<OfferDetailsModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.offer_id = offerId
            requestKeyHelper.display_id = DefaultHelper.decrypt("1234")
            /*println(
                "RequestHelper :" +
                        " ${requestKeyHelper.state} :" +
                        " ${requestKeyHelper.city} " +
                        ": ${requestKeyHelper.display_id}"
            )*/
            api.getOfferDetails(preferenceHelper.getJwtToken(), requestKeyHelper).enqueue(object :
                Callback<OfferDetailsModel> {
                override fun onResponse(
                    call: Call<OfferDetailsModel>,
                    response: Response<OfferDetailsModel>
                ) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    offerDetailsModel = gson?.fromJson(json, OfferDetailsModel::class.java)
                    println("$TAG : $json")
                    mutableLiveData.value = offerDetailsModel
                }

                override fun onFailure(call: Call<OfferDetailsModel>, t: Throwable) {
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