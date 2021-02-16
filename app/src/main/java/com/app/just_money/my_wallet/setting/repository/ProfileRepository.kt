package com.app.just_money.my_wallet.setting.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.RequestKeyHelper
import com.app.just_money.my_wallet.setting.model.GetUserProfileModel
import com.app.just_money.my_wallet.setting.model.SendEmailOtpModel
import com.app.just_money.my_wallet.setting.model.UpdatedProfileModel
import com.app.just_money.my_wallet.setting.model.VerifyEmailOtpModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileRepository {
    private val TAG = javaClass.simpleName
    private var getUserProfileModel: GetUserProfileModel? = null
    private var updateProfileModel: UpdatedProfileModel? = null
    private var sendEmailOtpModel: SendEmailOtpModel? = null
    private var verifyEmailOtpModel: VerifyEmailOtpModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getUserProfileData(context: Context, api: API): MutableLiveData<GetUserProfileModel> {
        val mutableLiveData: MutableLiveData<GetUserProfileModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            api.getUserProfileData(preferenceHelper.getJwtToken()).enqueue(object : Callback<GetUserProfileModel> {
                override fun onResponse(call: Call<GetUserProfileModel>, response: Response<GetUserProfileModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    getUserProfileModel = gson?.fromJson(json, GetUserProfileModel::class.java)
                    println("$TAG : $json")
                    mutableLiveData.value = getUserProfileModel
                }

                override fun onFailure(call: Call<GetUserProfileModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                }
            })
        } else {
            DefaultHelper.showToast(context, context.getString(R.string.no_internet))
        }
        return mutableLiveData
    }


    fun updateProfile(context: Context, api: API, name: String, lastName: String, dob: String, gender: String,
        email: String, uploadImage: File?): MutableLiveData<UpdatedProfileModel> {
        val mutableLiveData: MutableLiveData<UpdatedProfileModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            println("DefaultHelper: $email")
            val preferenceHelper = PreferenceHelper(context)
            val rbName: RequestBody = DefaultHelper.encrypt(name).toRequestBody("text/plain".toMediaTypeOrNull())
            val rbLastName: RequestBody =
                DefaultHelper.encrypt(lastName).toRequestBody("text/plain".toMediaTypeOrNull())
            val rbEmail: RequestBody = DefaultHelper.encrypt(email).toRequestBody("text/plain".toMediaTypeOrNull())
            val rbDob: RequestBody = DefaultHelper.encrypt(dob).toRequestBody("text/plain".toMediaTypeOrNull())
            val rbGender: RequestBody =
                DefaultHelper.encrypt(gender).toRequestBody("text/plain".toMediaTypeOrNull())
            var rbUploadImage: MultipartBody.Part? = null
            if (!chkFile(uploadImage)) {
                val requestFile: RequestBody =
                    uploadImage!!.asRequestBody("multipart/form-data".toMediaTypeOrNull())

                rbUploadImage = MultipartBody.Part.createFormData("profile_pic", uploadImage.name, requestFile)
            }

            api.updateProfile(preferenceHelper.getJwtToken(), rbUploadImage, rbName, rbLastName, rbGender, rbDob,
                rbEmail).enqueue(object : Callback<UpdatedProfileModel> {
                override fun onResponse(call: Call<UpdatedProfileModel>, response: Response<UpdatedProfileModel>) {
                    gson = gsonBuilder.create()
                    val json = Gson().toJson(response.body())
                    updateProfileModel = gson?.fromJson(json, UpdatedProfileModel::class.java)
                    println("$TAG : $json")
                    mutableLiveData.value = updateProfileModel
                }

                override fun onFailure(call: Call<UpdatedProfileModel>, t: Throwable) {
                    println("TAG : ${t.printStackTrace()}")
                }
            })
        } else {
            DefaultHelper.showToast(context, context.getString(R.string.no_internet))
        }
        return mutableLiveData
    }


    fun verifyEmailOtp(context: Context, api: API, otp: String): MutableLiveData<VerifyEmailOtpModel> {
        val mutableLiveData: MutableLiveData<VerifyEmailOtpModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.otp = DefaultHelper.encrypt(otp)
            api.verifyEmailOtp(preferenceHelper.getJwtToken(), requestKeyHelper)
                .enqueue(object : Callback<VerifyEmailOtpModel> {
                    override fun onResponse(call: Call<VerifyEmailOtpModel>,
                        response: Response<VerifyEmailOtpModel>) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        verifyEmailOtpModel = gson?.fromJson(json, VerifyEmailOtpModel::class.java)
                        println("$TAG : $json")
                        mutableLiveData.value = verifyEmailOtpModel
                    }

                    override fun onFailure(call: Call<VerifyEmailOtpModel>, t: Throwable) {
                        println("TAG : ${t.printStackTrace()}")
                    }
                })
        } else {
            DefaultHelper.showToast(context, context.getString(R.string.no_internet))
        }
        return mutableLiveData
    }

    private fun chkFile(file: File?): Boolean {
        return file == null
    }
}