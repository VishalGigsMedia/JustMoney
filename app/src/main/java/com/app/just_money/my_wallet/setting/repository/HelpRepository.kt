package com.app.just_money.my_wallet.setting.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultHelper.decrypt
import com.app.just_money.common_helper.DefaultHelper.showToast
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.setting.model.SendFeedbackModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HelpRepository {
    private val TAG = javaClass.simpleName
    private var sendFeedbackModel: SendFeedbackModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null


    fun sendFeedback(context: Context, apiService: API, name: String, email: String, subject: String,
        message: String, uploadImage: File?): MutableLiveData<SendFeedbackModel> {
        val mutableLiveData: MutableLiveData<SendFeedbackModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val authorizationToken = preferenceHelper.getJwtToken()
            val rbName: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), DefaultHelper.encrypt(name))
            val rbEmail: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), DefaultHelper.encrypt(email))
            val rbSubject: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), DefaultHelper.encrypt(subject))
            val rbMessage: RequestBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), DefaultHelper.encrypt(message))
            var rbUploadImage: MultipartBody.Part? = null
            if (!chkFile(uploadImage)) {
                val requestFile: RequestBody =
                    RequestBody.create("multipart/form-data".toMediaTypeOrNull(), uploadImage!!)
                rbUploadImage = MultipartBody.Part.createFormData("uploads", uploadImage.name, requestFile)
            }
            apiService.sendFeedback(authorizationToken, rbUploadImage, rbName, rbEmail, rbSubject, rbMessage)
                .enqueue(object : Callback<SendFeedbackModel> {
                    override fun onResponse(call: Call<SendFeedbackModel>, response: Response<SendFeedbackModel>) {
                        try {
                            //println(TAG + response.body())
                            gson = gsonBuilder.create()
                            val json = Gson().toJson(response.body())
                            sendFeedbackModel = gson?.fromJson(json, SendFeedbackModel::class.java)
                            if (sendFeedbackModel != null) {
                                mutableLiveData.value = sendFeedbackModel
                            } else {
                                showToast(context, decrypt(response.message()))
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    override fun onFailure(call: Call<SendFeedbackModel>, t: Throwable) {
                        //println(TAG + t.toString())
                        mutableLiveData.value = null
                    }
                })
        } else {
            showToast(context, context.getString(R.string.no_internet))
            mutableLiveData.value = null
        }
        return mutableLiveData
    }

    private fun chkFile(file: File?): Boolean {
        return file == null
    }
}