package com.app.just_money.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.login.model.GetOtpModel
import com.app.just_money.login.model.LoginModel
import com.app.just_money.login.model.LoginTrackier
import com.app.just_money.login.model.SignUpTrackier
import com.app.just_money.login.repository.LoginRepository

class LoginViewModel : ViewModel() {

    private var loginRepository: LoginRepository = LoginRepository()
    private var getOtpModel: LiveData<GetOtpModel>? = null
    private var loginModel: LiveData<LoginModel>? = null
    private var mutableLiveDataSignUpTrackier: LiveData<SignUpTrackier>? = null
    private var mutableLiveDataLoginTrackier: LiveData<LoginTrackier>? = null

    fun getOTP(
        activity: Context,
        api: API,
        mobile: String,
        countryCode: String
    ): LiveData<GetOtpModel> {
        if (getOtpModel == null || getOtpModel != null) {
            getOtpModel = loginRepository.getOTP(activity, api, mobile, countryCode)
        }
        return getOtpModel!!
    }

    fun login(
        activity: Context,
        api: API,
        mobile: String,
        otp: String,
        countryCode: String
    ): LiveData<LoginModel> {
        if (loginModel == null || loginModel != null) {
            loginModel = loginRepository.login(activity, api, mobile, otp, countryCode)
        }
        return loginModel!!
    }

    fun trackSignUp(
        activity: Context,
        name: String,
        email: String,
        password: String,
        phone: String,
        status: String
    ): LiveData<SignUpTrackier> {
        if (mutableLiveDataSignUpTrackier == null) {
            mutableLiveDataSignUpTrackier = loginRepository.trackSignUp(
                activity,
                name,
                email,
                password,
                phone,
                status
            )
        }
        return mutableLiveDataSignUpTrackier!!
    }

    fun trackLogin(
        context: Context,
        email: String,
        password: String
    ): LiveData<LoginTrackier> {
        if (mutableLiveDataLoginTrackier == null) {
            mutableLiveDataLoginTrackier = loginRepository.trackLogin(context, email, password)
        }
        return mutableLiveDataLoginTrackier!!
    }
}