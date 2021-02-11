package com.app.just_money.login.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.RequestKeyHelper
import com.app.just_money.login.model.RegisterUserModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterRepository {
    private val TAG = javaClass.simpleName
    private var registerUserModel: RegisterUserModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun register(context: Context?, api: API, firstName: String, lastName: String, emailId: String, password: String): MutableLiveData<RegisterUserModel> {
        val mutableLiveData: MutableLiveData<RegisterUserModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.firstname = DefaultHelper.encrypt(firstName)
            requestKeyHelper.lastname = DefaultHelper.encrypt(lastName)
            requestKeyHelper.email = DefaultHelper.encrypt(emailId)
            requestKeyHelper.password = DefaultHelper.encrypt(password)
            requestKeyHelper.device_id = DefaultHelper.encrypt(DefaultHelper.getDeviceId(context!!))
            requestKeyHelper.invite_code = "" //DefaultHelper.encrypt(DefaultHelper.getPackageId(context))
            api.register(requestKeyHelper).enqueue(object : Callback<RegisterUserModel> {
                override fun onResponse(call: Call<RegisterUserModel>, response: Response<RegisterUserModel>) {
                    val header = response.headers()["Authorization"].toString()
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    registerUserModel = gson?.fromJson(json, RegisterUserModel::class.java)
                    mutableLiveData.value = registerUserModel
                    /* if (header.isNotEmpty()) {
                         setPreferenceValue(context, registerUserModel!!, header)
                     }*/

                }

                override fun onFailure(call: Call<RegisterUserModel>, t: Throwable) {
                    //println("TAG : ${t.printStackTrace()}")
                }
            })
        } else {
            //DefaultHelper.showToast(context, context.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

    private fun setPreferenceValue(context: Context, registerUserModel: RegisterUserModel, header: String) {
        val preferenceHelper = PreferenceHelper(context)
        if (header.isNotEmpty()) {
            preferenceHelper.setJwtToken("Bearer $header")
        }
        /*if (registerUserModel.registerData?.userId.toString().isNotEmpty()) {
            preferenceHelper.setUserId(registerUserModel.registerData?.userId.toString())
        }*/
        preferenceHelper.setUserLoggedIn(true)
    }

}