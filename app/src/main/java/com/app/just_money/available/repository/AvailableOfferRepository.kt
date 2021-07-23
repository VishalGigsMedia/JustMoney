package com.app.just_money.available.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.available.model.AvailableOfferModel
import com.app.just_money.available.model.IpApiModel
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultHelper.encrypt
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.common_helper.TrackingEvents.trackOfferEarned
import com.app.just_money.dagger.API
import com.app.just_money.dagger.RequestKeyHelper
import com.app.just_money.login.model.ClaimOfferModel
import com.app.just_money.model.CheckAppVersionModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AvailableOfferRepository {
    private val tag = javaClass.simpleName
    private var availableOfferModel: AvailableOfferModel? = null
    private var claimOfferModel: ClaimOfferModel? = null
    private var checkAppVersionModel: CheckAppVersionModel? = null
    private var ipAddressModel: IpApiModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getOffers(context: Context?, api: API): MutableLiveData<AvailableOfferModel> {
        val mutableLiveData: MutableLiveData<AvailableOfferModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.country = encrypt(preferenceHelper.getUserCountry())
            requestKeyHelper.country_code = encrypt(preferenceHelper.getUserCountryCode())
            requestKeyHelper.state = encrypt(preferenceHelper.getUserState())
            requestKeyHelper.state_code = encrypt(preferenceHelper.getUserStateCode())
            requestKeyHelper.city = encrypt(preferenceHelper.getUserCity())
            requestKeyHelper.user_click_ip = encrypt(preferenceHelper.getIpAddress())
            requestKeyHelper.fcm_key = encrypt(preferenceHelper.getFCMToken())
            api.getOffers(preferenceHelper.getJwtToken(), requestKeyHelper)
                .enqueue(object : Callback<AvailableOfferModel> {
                    override fun onResponse(call: Call<AvailableOfferModel>,
                        response: Response<AvailableOfferModel>) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        availableOfferModel = gson?.fromJson(json, AvailableOfferModel::class.java)
                        println("$tag : $json")
                        mutableLiveData.value = availableOfferModel
                    }

                    override fun onFailure(call: Call<AvailableOfferModel>, t: Throwable) {
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


    fun claimOffer(context: Context?, api: API, appId: String): MutableLiveData<ClaimOfferModel> {
        val mutableLiveData: MutableLiveData<ClaimOfferModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.appId = appId
            requestKeyHelper.user_click_ip = encrypt(preferenceHelper.getIpAddress())
            api.claimOffer(preferenceHelper.getJwtToken(), requestKeyHelper)
                .enqueue(object : Callback<ClaimOfferModel> {
                    override fun onResponse(call: Call<ClaimOfferModel>, response: Response<ClaimOfferModel>) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        claimOfferModel = gson?.fromJson(json, ClaimOfferModel::class.java)
                        println("$tag : $json")
                        mutableLiveData.value = claimOfferModel
                        trackOfferEarned(appId)
                    }

                    override fun onFailure(call: Call<ClaimOfferModel>, t: Throwable) {
                        println("TAG : ${t.printStackTrace()}")
                    }
                })
        } else {
            DefaultHelper.showToast(context, context?.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

    fun claimReward(context: Context?, api: API, rewardAmount: String): MutableLiveData<ClaimOfferModel> {
        val mutableLiveData: MutableLiveData<ClaimOfferModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.reward_amount = rewardAmount
            requestKeyHelper.user_click_ip = encrypt(preferenceHelper.getIpAddress())
            api.claimReward(preferenceHelper.getJwtToken(), requestKeyHelper)
                .enqueue(object : Callback<ClaimOfferModel> {
                    override fun onResponse(call: Call<ClaimOfferModel>, response: Response<ClaimOfferModel>) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        claimOfferModel = gson?.fromJson(json, ClaimOfferModel::class.java)
                        println("$tag : $json")
                        mutableLiveData.value = claimOfferModel
                    }

                    override fun onFailure(call: Call<ClaimOfferModel>, t: Throwable) {
                        mutableLiveData.value = null
                        println("TAG : ${t.printStackTrace()}")
                    }
                })
        } else {
            mutableLiveData.value = null
            DefaultHelper.showToast(context, context?.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

    fun checkVersion(context: Context?, api: API): MutableLiveData<CheckAppVersionModel> {
        val mutableLiveData: MutableLiveData<CheckAppVersionModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val authorizationToken = preferenceHelper.getJwtToken()
            api.checkVersion(authorizationToken).enqueue(object : Callback<CheckAppVersionModel> {
                override fun onResponse(call: Call<CheckAppVersionModel>,
                    response: Response<CheckAppVersionModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    checkAppVersionModel = gson?.fromJson(json, CheckAppVersionModel::class.java)
                    // println("TAG : $json")
                    mutableLiveData.value = checkAppVersionModel
                }

                override fun onFailure(call: Call<CheckAppVersionModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                }
            })
        } else {
            DefaultHelper.showToast(context, context?.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

    fun getIPAddress(context: Context?, api: API): MutableLiveData<IpApiModel> {
        val mutableLiveData: MutableLiveData<IpApiModel> = MutableLiveData()
        val url = "http://ip-api.com/json/"
        api.getIPAddress(url).enqueue(object : Callback<IpApiModel> {
            override fun onResponse(call: Call<IpApiModel>, response: Response<IpApiModel>) {
                /*gson = gsonBuilder.create()
                val json = Gson().toJson(response.body())
                ipAddressModel = gson?.fromJson(json, IPAPIModel::class.java)*/
                if (response.body() != null && response.isSuccessful && response.code() == 200) {
                    Log.d("ipaidd", "onResponse: ${response.body()}")
                    mutableLiveData.value = response.body()
                } else mutableLiveData.value = null
            }

            override fun onFailure(call: Call<IpApiModel>, t: Throwable) {
                println("TAG : ${t.printStackTrace()}")
                mutableLiveData.value = null
            }
        })
        return mutableLiveData
    }

}