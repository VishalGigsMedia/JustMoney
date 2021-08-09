package com.app.cent4free.my_wallet.completed

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper
import com.app.cent4free.common_helper.DefaultHelper.encrypt
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.RequestKeyHelper
import com.app.cent4free.my_wallet.completed.model.CompletedOfferModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CompletedOfferRepository {

    private val TAG = javaClass.simpleName
    private var completedOfferModel: CompletedOfferModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getCompletedOffers(context: Context?, api: API, scroll_offset: Int, scroll_limit: Int): MutableLiveData<CompletedOfferModel> {
        val mutableLiveData: MutableLiveData<CompletedOfferModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.scroll_offset = encrypt(scroll_offset.toString())
            requestKeyHelper.scroll_limit = encrypt(scroll_limit.toString())
            api.getCompletedOffers(preferenceHelper.getJwtToken(), requestKeyHelper)
                .enqueue(object : Callback<CompletedOfferModel> {
                    override fun onResponse(call: Call<CompletedOfferModel>,
                        response: Response<CompletedOfferModel>) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        completedOfferModel = gson?.fromJson(json, CompletedOfferModel::class.java)
                        mutableLiveData.value = completedOfferModel
                    }

                    override fun onFailure(call: Call<CompletedOfferModel>, t: Throwable) {
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