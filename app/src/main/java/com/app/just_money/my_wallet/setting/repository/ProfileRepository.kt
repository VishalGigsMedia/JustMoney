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
import com.app.just_money.my_wallet.setting.model.UpdateProfileModel
import com.app.just_money.my_wallet.setting.model.VerifyEmailOtpModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileRepository {
    private val TAG = javaClass.simpleName
    private var getUserProfileModel: GetUserProfileModel? = null
    private var updateProfileModel: UpdateProfileModel? = null
    private var sendEmailOtpModel: SendEmailOtpModel? = null
    private var verifyEmailOtpModel: VerifyEmailOtpModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null

    fun getUserProfileData(
        context: Context,
        api: API
    ): MutableLiveData<GetUserProfileModel> {
        val mutableLiveData: MutableLiveData<GetUserProfileModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            api.getUserProfileData(preferenceHelper.getJwtToken())
                .enqueue(object : Callback<GetUserProfileModel> {
                    override fun onResponse(
                        call: Call<GetUserProfileModel>,
                        response: Response<GetUserProfileModel>
                    ) {
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
            DefaultHelper.showToast(
                context,
                context.getString(R.string.no_internet)
            )
        }
        return mutableLiveData
    }


    fun updateProfile(
        context: Context,
        api: API,
        name: String,
        lastName: String,
        dob: String,
        gender: String,
        email: String,
        uploadImage: File?
    ): MutableLiveData<UpdateProfileModel> {
        val mutableLiveData: MutableLiveData<UpdateProfileModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            println("DefaultHelper: $email")
            val preferenceHelper = PreferenceHelper(context)
            val rbName: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), DefaultHelper.encrypt(name))
            val rbLastName: RequestBody =
                RequestBody.create(
                    "text/plain".toMediaTypeOrNull(),
                    DefaultHelper.encrypt(lastName)
                )
            val rbEmail: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), DefaultHelper.encrypt(email))
            val rbDob: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), DefaultHelper.encrypt(dob))
            val rbGender: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), DefaultHelper.encrypt(gender))
            var rbUploadImage: MultipartBody.Part? = null
            if (!chkFile(uploadImage)) {
                val requestFile: RequestBody =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), uploadImage!!)

                rbUploadImage =
                    MultipartBody.Part.createFormData("profile_pic", uploadImage.name, requestFile)
            }

            api.updateProfile(
                preferenceHelper.getJwtToken(),
                rbUploadImage,
                rbName,
                rbLastName,
                rbGender,
                rbDob,
                rbEmail
            )
                .enqueue(object : Callback<UpdateProfileModel> {
                    override fun onResponse(
                        call: Call<UpdateProfileModel>,
                        response: Response<UpdateProfileModel>
                    ) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        updateProfileModel = gson?.fromJson(json, UpdateProfileModel::class.java)
                        println("$TAG : $json")
                        mutableLiveData.value = updateProfileModel
                    }

                    override fun onFailure(call: Call<UpdateProfileModel>, t: Throwable) {
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


    fun sendEmailVerificationOtp(
        context: Context,
        api: API
    ): MutableLiveData<SendEmailOtpModel> {
        val mutableLiveData: MutableLiveData<SendEmailOtpModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            api.sendEmailVerificationOtp(preferenceHelper.getJwtToken())
                .enqueue(object : Callback<SendEmailOtpModel> {
                    override fun onResponse(
                        call: Call<SendEmailOtpModel>,
                        response: Response<SendEmailOtpModel>
                    ) {
                        gson = gsonBuilder.create()
                        val json = Gson().toJson(response.body())
                        sendEmailOtpModel = gson?.fromJson(json, SendEmailOtpModel::class.java)
                        println("$TAG : $json")
                        mutableLiveData.value = sendEmailOtpModel
                    }

                    override fun onFailure(call: Call<SendEmailOtpModel>, t: Throwable) {
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

    fun verifyEmailOtp(
        context: Context,
        api: API,
        otp: String
    ): MutableLiveData<VerifyEmailOtpModel> {
        val mutableLiveData: MutableLiveData<VerifyEmailOtpModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val requestKeyHelper = RequestKeyHelper()
            requestKeyHelper.otp = DefaultHelper.encrypt(otp)
            api.verifyEmailOtp(preferenceHelper.getJwtToken(), requestKeyHelper)
                .enqueue(object : Callback<VerifyEmailOtpModel> {
                    override fun onResponse(
                        call: Call<VerifyEmailOtpModel>,
                        response: Response<VerifyEmailOtpModel>
                    ) {
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
            DefaultHelper.showToast(
                context,
                context.getString(R.string.no_internet)
            )
        }
        return mutableLiveData
    }

    private fun chkFile(file: File?): Boolean {
        return file == null
    }
}