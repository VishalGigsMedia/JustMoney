package com.app.just_money.login.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.RequestKeyHelper
import com.app.just_money.login.model.GetOtpModel
import com.app.just_money.login.model.LoginModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginRepository {

    private val TAG = javaClass.simpleName
    private var getOtpModel: GetOtpModel? = null
    private var loginModel: LoginModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getOTP(
        context: Context,
        api: API,
        mobile: String,
        countryCode: String
    ): MutableLiveData<GetOtpModel> {
        val mutableLiveData: MutableLiveData<GetOtpModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.mobile = DefaultHelper.encrypt(mobile)
            requestKeyHelper.package_id = DefaultHelper.getPackageId(context)
            requestKeyHelper.country_code = DefaultHelper.encrypt(countryCode)
            /*println(
                "RequestHelper :" +
                        " ${requestKeyHelper.mobile} :" +
                        " ${requestKeyHelper.package_id} " +
                        ": ${requestKeyHelper.country_code}"
            )*/
            api.getOTP(requestKeyHelper).enqueue(object : Callback<GetOtpModel> {
                override fun onResponse(call: Call<GetOtpModel>, response: Response<GetOtpModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    getOtpModel = gson?.fromJson(json, GetOtpModel::class.java)
                    println("$TAG : $json")
                    mutableLiveData.value = getOtpModel
                }

                override fun onFailure(call: Call<GetOtpModel>, t: Throwable) {
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


    fun login(
        context: Context,
        api: API,
        mobile: String,
        otp: String,
        countryCode: String
    ): MutableLiveData<LoginModel> {
        val mutableLiveData: MutableLiveData<LoginModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.mobile = DefaultHelper.encrypt(mobile)
            requestKeyHelper.fcm_key = ""
            requestKeyHelper.carrier_name = DefaultHelper.getCarrierName(context)
            requestKeyHelper.cpu = DefaultHelper.getCpu()
            requestKeyHelper.version_name = DefaultHelper.getVersionName()
            requestKeyHelper.version_code = DefaultHelper.getVersionCode()
            requestKeyHelper.device_type = DefaultHelper.getDeviceType()
            requestKeyHelper.display = DefaultHelper.getDisplay()
            requestKeyHelper.device = DefaultHelper.getBrand()
            requestKeyHelper.device_id = DefaultHelper.encrypt(DefaultHelper.getDeviceId(context))
            requestKeyHelper.android_version = DefaultHelper.getBuildVersion()
            requestKeyHelper.country_code = DefaultHelper.encrypt(countryCode)
            requestKeyHelper.token = DefaultHelper.encrypt(otp)
            /* println(
                 "RequestHelper :" +
                         "mobile:${requestKeyHelper.mobile}" +
                         "\ncarrier_name :${requestKeyHelper.carrier_name} " +
                         "\ncpu : ${requestKeyHelper.cpu} " +
                         "\nversion_name : ${requestKeyHelper.version_name} " +
                         "\nversion_code : ${requestKeyHelper.version_code} " +
                         "\ndevice_type : ${requestKeyHelper.device_type} " +
                         "\ndisplay : ${requestKeyHelper.display} " +
                         "\ndevice : ${requestKeyHelper.device} " +
                         "\ndevice_id : ${requestKeyHelper.device_id} " +
                         "\nandroid_version : ${requestKeyHelper.android_version} " +
                         "\ncountry_code : ${requestKeyHelper.country_code} " +
                         "\ntoken : ${requestKeyHelper.token}"
             )*/
            api.login(requestKeyHelper).enqueue(object : Callback<LoginModel> {
                override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                    val header = response.headers()["Authorization"].toString()
                    println("Header: $header")
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    loginModel = gson?.fromJson(json, LoginModel::class.java)
                    println("$TAG : $json")
                    if (loginModel != null && header.isNotEmpty()) {
                        setPreferenceValue(context, loginModel!!, header)
                    }
                    mutableLiveData.value = loginModel
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
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

    private fun setPreferenceValue(context: Context, loginModel: LoginModel, header: String) {
        val preferenceHelper = PreferenceHelper(context)
        if (header.isNotEmpty()) {
            preferenceHelper.setJwtToken("Bearer $header")
        }
        if (loginModel.data?.userId.toString().isNotEmpty()) {
            preferenceHelper.setUserId(loginModel.data?.userId.toString())
        }
        preferenceHelper.setUserLoggedIn(true)
    }
}