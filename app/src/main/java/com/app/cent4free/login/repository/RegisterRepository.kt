package com.app.cent4free.login.repository

import android.content.Context
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.app.cent4free.BuildConfig
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper
import com.app.cent4free.common_helper.DefaultHelper.encrypt
import com.app.cent4free.common_helper.DefaultHelper.getDeviceId
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.RequestKeyHelper
import com.app.cent4free.login.model.RegisterUserModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository {
    private var registerUserModel: RegisterUserModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun register(context: Context?, api: API, firstName: String, lastName: String, emailId: String,
        password: String, refCode: String): MutableLiveData<RegisterUserModel> {
        val mutableLiveData: MutableLiveData<RegisterUserModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val requestKeyHelper = RequestKeyHelper()
            val preferenceHelper = PreferenceHelper(context)
            requestKeyHelper.firstname = encrypt(firstName)
            requestKeyHelper.lastname = encrypt(lastName)
            requestKeyHelper.email = encrypt(emailId)
            requestKeyHelper.password = encrypt(password)
            requestKeyHelper.device_id = encrypt(getDeviceId(context!!))
            requestKeyHelper.invite_code = encrypt(refCode)
            requestKeyHelper.user_click_ip = encrypt(preferenceHelper.getIpAddress())
            requestKeyHelper.android_version = encrypt(Build.VERSION.RELEASE)
            requestKeyHelper.cpu = encrypt(Build.CPU_ABI)
            requestKeyHelper.version_name = encrypt(BuildConfig.VERSION_NAME)
            requestKeyHelper.version_code = encrypt(BuildConfig.VERSION_CODE.toString())
            requestKeyHelper.device_type = encrypt("phone")
            requestKeyHelper.display = encrypt(Build.DISPLAY)
            requestKeyHelper.device_manufacturer = encrypt(Build.MANUFACTURER)
            requestKeyHelper.device_brand = encrypt(Build.BRAND)
            requestKeyHelper.device_model = encrypt(Build.MODEL)
            api.register(requestKeyHelper).enqueue(object : Callback<RegisterUserModel> {
                override fun onResponse(call: Call<RegisterUserModel>, response: Response<RegisterUserModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    registerUserModel = gson?.fromJson(json, RegisterUserModel::class.java)
                    mutableLiveData.value = registerUserModel
                }

                override fun onFailure(call: Call<RegisterUserModel>, t: Throwable) {
                    mutableLiveData.value = null
                    //println("TAG : ${t.printStackTrace()}")
                }
            })
        } else {
            mutableLiveData.value = null
            showToast(context, context?.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

}