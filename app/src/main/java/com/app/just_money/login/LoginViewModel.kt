package com.app.just_money.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.dagger.API
import com.app.just_money.login.model.GetOtpModel
import com.app.just_money.login.model.LoginModel
import com.app.just_money.login.repository.LoginRepository

class LoginViewModel : ViewModel() {

    private var loginRepository: LoginRepository = LoginRepository()
    private var getOtpModel: LiveData<GetOtpModel>? = null
    private var loginModel: LiveData<LoginModel>? = null

    fun getOTP(
        activity: Context,
        api: API,
        mobile: String,
        countryCode: String
    ): LiveData<GetOtpModel> {
        if (getOtpModel == null || getOtpModel != null) {
            getOtpModel = loginRepository.getOTP(activity, api, mobile,countryCode)
        }
        return getOtpModel!!
    }

    fun login(
        activity: Context,
        api: API,
        mobile: String,
        otp: String,
        countryCode:String
    ): LiveData<LoginModel> {
        if (loginModel == null || loginModel != null) {
            loginModel = loginRepository.login(activity, api, mobile,otp,countryCode)
        }
        return loginModel!!
    }
}