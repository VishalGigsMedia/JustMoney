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
import com.app.cent4free.common_helper.DefaultKeyHelper
import com.app.cent4free.common_helper.DefaultKeyHelper.APP_LOGIN
import com.app.cent4free.common_helper.DefaultKeyHelper.GOOGLE_LOGIN
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.ApiClient
import com.app.cent4free.dagger.RequestKeyHelper
import com.app.cent4free.login.model.ForgotPasswordModel
import com.app.cent4free.login.model.GetOtpModel
import com.app.cent4free.login.model.LoginTrackier
import com.app.cent4free.login.model.SignUpTrackier
import com.app.cent4free.login.model.login.LoginModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginRepository {

    private val TAG = javaClass.simpleName
    private var getOtpModel: GetOtpModel? = null
    private var loginModel: LoginModel? = null
    private var forgotPasswordModel: ForgotPasswordModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    var signUpTrackier: SignUpTrackier? = null
    var loginTrackier: LoginTrackier? = null
    val Hash_file_maps2 = HashMap<String, String>()
    val hashFileMapsContentType = HashMap<String, String>()
    val hashFileMapsAuthorization = HashMap<String, String>()


    fun forgotPassword(context: Context, api: API, mobile: String, countryCode: String): MutableLiveData<GetOtpModel> {
        val mutableLiveData: MutableLiveData<GetOtpModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.mobile = encrypt(mobile)
            requestKeyHelper.package_id = DefaultHelper.getPackageId(context)
            requestKeyHelper.country_code = encrypt(countryCode)
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
                    mutableLiveData.value = getOtpModel
                }

                override fun onFailure(call: Call<GetOtpModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                }
            })
        } else {
            showToast(context, context.getString(R.string.no_internet))
        }
        return mutableLiveData
    }


    fun login(context: Context, api: API, login_type: String, email: String, password_or_id: String,
        carrierName: String): MutableLiveData<LoginModel> {
        val mutableLiveData: MutableLiveData<LoginModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val requestKeyHelper = RequestKeyHelper()
            val preferenceHelper = PreferenceHelper(context)
            requestKeyHelper.login_type = encrypt(login_type)
            requestKeyHelper.email = encrypt(email)
            when (login_type) {
                APP_LOGIN -> requestKeyHelper.password = encrypt(password_or_id)
                GOOGLE_LOGIN -> requestKeyHelper.google_social_id = encrypt(password_or_id)
            }
            requestKeyHelper.device_brand = encrypt(Build.BRAND)
            requestKeyHelper.device_model = encrypt(Build.MODEL)
            requestKeyHelper.android_version = encrypt(Build.VERSION.RELEASE)
            requestKeyHelper.version_name = encrypt(BuildConfig.VERSION_NAME)
            requestKeyHelper.version_code = encrypt(BuildConfig.VERSION_CODE.toString())
            requestKeyHelper.cpu = encrypt(Build.CPU_ABI)
            requestKeyHelper.display = encrypt(Build.DISPLAY)
            requestKeyHelper.device_type = encrypt("phone")
            requestKeyHelper.device_id = encrypt(getDeviceId(context))
            requestKeyHelper.carrier_name = encrypt(carrierName)
            requestKeyHelper.device_manufacturer = encrypt(Build.MANUFACTURER)
            requestKeyHelper.user_click_ip = encrypt(preferenceHelper.getIpAddress())
            api.login(requestKeyHelper).enqueue(object : Callback<LoginModel> {
                override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
                    val header = response.headers()["Authorization"].toString()
                    preferenceHelper.setJwtToken("Bearer $header")
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    loginModel = gson?.fromJson(json, LoginModel::class.java)
                    mutableLiveData.value = loginModel
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    //println("TAG : ${t.printStackTrace()}")
                    mutableLiveData.value = null
                }
            })
        } else {
            mutableLiveData.value = null
            showToast(context, context.getString(R.string.no_internet))
        }
        return mutableLiveData
    }


    fun trackSignUp(context: Context, name: String, email: String, password: String, phone: String, status: String): MutableLiveData<SignUpTrackier> {
        val mutableLiveData: MutableLiveData<SignUpTrackier> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            hashFileMapsContentType.put("Content-Type", "application/json")
            hashFileMapsAuthorization.put("X-Api-Key", DefaultKeyHelper.xApiKey)

            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.name = name
            requestKeyHelper.email = email
            requestKeyHelper.password = password
            requestKeyHelper.phone = phone
            requestKeyHelper.status = status
            val apiInterface: API? = ApiClient.getClient(context)?.create(API::class.java)
            apiInterface?.trackSignUp(hashFileMapsContentType, hashFileMapsAuthorization, requestKeyHelper)
                ?.enqueue(object : Callback<SignUpTrackier> {
                    override fun onResponse(call: Call<SignUpTrackier>, response: Response<SignUpTrackier>) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        signUpTrackier = gson?.fromJson(json, SignUpTrackier::class.java)
                        println("$TAG : $json")
                        mutableLiveData.value = signUpTrackier
                    }

                    override fun onFailure(call: Call<SignUpTrackier>, t: Throwable) {
                        println("TAG : ${t.printStackTrace()}")
                    }
                })
        } else {
            showToast(context, context.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

    fun trackLogin(context: Context, email: String, password: String): MutableLiveData<LoginTrackier> {
        val mutableLiveData: MutableLiveData<LoginTrackier> = MutableLiveData()
        //val loginRequest1 = UserModel(phonenumber, phonenumber, UtilsDefault.GIGSNATIVEURL)
        hashFileMapsContentType.put("Content-Type", "application/json")

        val requestKeyHelper = RequestKeyHelper()
        requestKeyHelper.email = email
        requestKeyHelper.password = password
        requestKeyHelper.networkUrl = DefaultKeyHelper.GIGS_NATIVE_URL
        val apiInterface: API? = ApiClient.getClient(context)?.create(API::class.java)
        val call: Call<LoginTrackier>? = apiInterface?.trackLogin(Hash_file_maps2, requestKeyHelper)
        call!!.enqueue(object : Callback<LoginTrackier?> {
            override fun onResponse(call: Call<LoginTrackier?>?, response: Response<LoginTrackier?>) {
                gson = gsonBuilder.create()
                val json = Gson().toJson(response.body())
                loginTrackier = gson?.fromJson(json, LoginTrackier::class.java)
                println("$TAG : $json")
                mutableLiveData.value = loginTrackier
            }

            override fun onFailure(call: Call<LoginTrackier?>, t: Throwable) {
                //   t.printStackTrace()
            }
        })
        return mutableLiveData
    }


    fun forgotPassword(context: Context, api: API, emailId: String): MutableLiveData<ForgotPasswordModel> {

        val mutableLiveData: MutableLiveData<ForgotPasswordModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.email = encrypt(emailId)
            api.forgotPassword(requestKeyHelper).enqueue(object : Callback<ForgotPasswordModel> {
                override fun onResponse(call: Call<ForgotPasswordModel>, response: Response<ForgotPasswordModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    forgotPasswordModel = gson?.fromJson(json, ForgotPasswordModel::class.java)
                    mutableLiveData.value = forgotPasswordModel
                }

                override fun onFailure(call: Call<ForgotPasswordModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                }
            })
        } else {
            showToast(context, context.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

}