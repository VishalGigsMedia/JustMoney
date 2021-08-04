package com.app.cent4free.offer_details.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.RequestKeyHelper
import com.app.cent4free.login.model.ClaimOfferModel
import com.app.cent4free.offer_details.model.OfferDetailsModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OfferDetailsRepository {

    private val TAG = javaClass.simpleName
    private var offerDetailsModel: OfferDetailsModel? = null
    private var claimOfferModel: ClaimOfferModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getOfferDetails(context: Context?, api: API, offer_trackier_id: String): MutableLiveData<OfferDetailsModel> {
        val mutableLiveData: MutableLiveData<OfferDetailsModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.offer_trackier_id = offer_trackier_id

            api.getOfferDetails(preferenceHelper.getJwtToken(), requestKeyHelper)
                .enqueue(object : Callback<OfferDetailsModel> {
                    override fun onResponse(call: Call<OfferDetailsModel>, response: Response<OfferDetailsModel>) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        offerDetailsModel = gson?.fromJson(json, OfferDetailsModel::class.java)
                        println("$TAG : $json")
                        mutableLiveData.value = offerDetailsModel
                    }

                    override fun onFailure(call: Call<OfferDetailsModel>, t: Throwable) {
                        println("TAG : ${t.printStackTrace()}")
                        mutableLiveData.value = null
                    }
                })
        } else {
            DefaultHelper.showToast(context, context!!.getString(R.string.no_internet))
            mutableLiveData.value = null
        }
        return mutableLiveData
    }


}