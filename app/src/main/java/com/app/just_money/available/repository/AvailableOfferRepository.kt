package com.app.just_money.available.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.available.model.AvailableOfferModel
import com.app.just_money.available.model.IpAddressModel
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.common_helper.TrackingEvents
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
    private var ipAddressModel: IpAddressModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getOffers(context: Context?, api: API): MutableLiveData<AvailableOfferModel> {
        val mutableLiveData: MutableLiveData<AvailableOfferModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.country = /*preferenceHelper.getUserState()*/"vNWcbZ07F9T9Fa0LjxaRsA=="
            requestKeyHelper.display_id = DefaultHelper.encrypt("1234")
            api.getOffers(preferenceHelper.getJwtToken(), requestKeyHelper)
                .enqueue(object : Callback<AvailableOfferModel> {
                    override fun onResponse(call: Call<AvailableOfferModel>, response: Response<AvailableOfferModel>) {
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
            requestKeyHelper.user_click_ip = DefaultHelper.encrypt(preferenceHelper.getIpAddress())
            api.claimOffer(preferenceHelper.getJwtToken(), requestKeyHelper)
                .enqueue(object : Callback<ClaimOfferModel> {
                    override fun onResponse(call: Call<ClaimOfferModel>, response: Response<ClaimOfferModel>) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        claimOfferModel = gson?.fromJson(json, ClaimOfferModel::class.java)
                        println("$tag : $json")
                        mutableLiveData.value = claimOfferModel
                        TrackingEvents.trackOfferEarned(appId)
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
            val preferenceHelper =PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.reward_amount = rewardAmount
            requestKeyHelper.user_click_ip = DefaultHelper.encrypt(preferenceHelper.getIpAddress())
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
                override fun onResponse(call: Call<CheckAppVersionModel>, response: Response<CheckAppVersionModel>) {
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

    fun getIPAddress(context: Context?, api: API): MutableLiveData<IpAddressModel> {
        val mutableLiveData: MutableLiveData<IpAddressModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val url = "https://api.ipify.org/?format=json"
            api.getIPAddress(url).enqueue(object : Callback<IpAddressModel> {
                override fun onResponse(call: Call<IpAddressModel>, response: Response<IpAddressModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    ipAddressModel = gson?.fromJson(json, IpAddressModel::class.java)
                    // println("TAG : $json")
                    mutableLiveData.value = ipAddressModel
                }

                override fun onFailure(call: Call<IpAddressModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                }
            })
        } else {
            DefaultHelper.showToast(context, context?.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

}