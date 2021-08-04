package com.app.cent4free.my_wallet.setting.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.dagger.API
import com.app.cent4free.my_wallet.setting.model.GetUserProfileModel
import com.app.cent4free.my_wallet.setting.model.SendEmailOtpModel
import com.app.cent4free.my_wallet.setting.model.UpdatedProfileModel
import com.app.cent4free.my_wallet.setting.model.VerifyEmailOtpModel
import com.app.cent4free.my_wallet.setting.repository.ProfileRepository
import java.io.File

class ProfileViewModel : ViewModel() {
    private val profileRepository: ProfileRepository = ProfileRepository()
    private var getUserProfileModel: LiveData<GetUserProfileModel>? = null
    private var updateProfileModel: LiveData<UpdatedProfileModel>? = null
    private var sendEmailOtpModel: LiveData<SendEmailOtpModel>? = null
    private var verifyEmailOtpModel: LiveData<VerifyEmailOtpModel>? = null

    fun getUserProfile(context: Context, api: API): LiveData<GetUserProfileModel> {
        if (getUserProfileModel != null || getUserProfileModel == null) {
            getUserProfileModel = profileRepository.getUserProfileData(context, api)
        }
        return getUserProfileModel!!
    }

    fun updateProfile(context: Context, api: API, name: String, lastName: String, dob: String, gender: String,
        email: String,mobile: String, uploadImage: File?): LiveData<UpdatedProfileModel> {
        if (updateProfileModel != null || updateProfileModel == null) {
            updateProfileModel =
                profileRepository.updateProfile(context, api, name, lastName, dob, gender, email,mobile, uploadImage)
        }
        return updateProfileModel as LiveData<UpdatedProfileModel>
    }


    fun verifyEmailOtp(context: Context, api: API, otp: String): LiveData<VerifyEmailOtpModel> {
        if (verifyEmailOtpModel != null || verifyEmailOtpModel == null) {
            verifyEmailOtpModel = profileRepository.verifyEmailOtp(context, api, otp)
        }
        return verifyEmailOtpModel!!
    }
}