package com.app.just_money.my_wallet.setting.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultHelper.decrypt
import com.app.just_money.common_helper.DefaultHelper.encrypt
import com.app.just_money.common_helper.DefaultHelper.getCompressed
import com.app.just_money.common_helper.DefaultHelper.showToast
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.setting.model.SendFeedbackModel
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
import java.util.*

class HelpRepository {
    private val TAG = javaClass.simpleName
    private var sendFeedbackModel: SendFeedbackModel? = null
    private val gsonBuilder = GsonBuilder()
    private var gson: Gson? = null


    fun sendFeedback(context: Context, apiService: API, name: String, email: String, subject: String,
        message: String, uploadImage: ArrayList<File>): MutableLiveData<SendFeedbackModel> {
        val mutableLiveData: MutableLiveData<SendFeedbackModel> = MutableLiveData()
        if (DefaultHelper.isOnline()) {
            val preferenceHelper = PreferenceHelper(context)
            val authorizationToken = preferenceHelper.getJwtToken()
            val rbName: RequestBody = encrypt(name).toRequestBody("text/plain".toMediaTypeOrNull())
            val rbEmail: RequestBody = encrypt(email).toRequestBody("text/plain".toMediaTypeOrNull())
            val rbSubject: RequestBody = encrypt(subject).toRequestBody("text/plain".toMediaTypeOrNull())
            val rbMessage: RequestBody = encrypt(message).toRequestBody("text/plain".toMediaTypeOrNull())

            val imageParts = ArrayList<MultipartBody.Part>()
            var file: File? = null
            for (i in 0 until uploadImage.size) {
                file = File(getCompressed(context, uploadImage[i].path,i).toString())
                if (!chkFile(file)) {
                    println("imagePath: $file")
                    imageParts.add(imagePrepareFilePart(file,i))
                }
            }

            apiService.sendFeedback(authorizationToken, rbName, rbEmail, rbSubject, rbMessage, imageParts)
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

    private fun imagePrepareFilePart(fileName: File, i: Int): MultipartBody.Part {
        val requestFile = fileName.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("uploads[]", fileName.name, requestFile)
    }
}