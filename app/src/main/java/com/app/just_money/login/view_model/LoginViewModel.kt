package com.app.just_money.login.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.dagger.API
import com.app.just_money.login.model.ForgotPasswordModel
import com.app.just_money.login.model.GetOtpModel
import com.app.just_money.login.model.LoginTrackier
import com.app.just_money.login.model.SignUpTrackier
import com.app.just_money.login.model.login.LoginModel
import com.app.just_money.login.repository.LoginRepository

class LoginViewModel : ViewModel() {

    private var loginRepository: LoginRepository = LoginRepository()
    private var getOtpModel: LiveData<GetOtpModel>? = null
    private var loginModel: LiveData<LoginModel>? = null
    private var forgotPasswordModel: LiveData<ForgotPasswordModel>? = null
    private var mutableLiveDataSignUpTrackier: LiveData<SignUpTrackier>? = null
    private var mutableLiveDataLoginTrackier: LiveData<LoginTrackier>? = null
    private var isValidPassword: LiveData<Boolean>? = null

    fun getOTP(activity: Context, api: API, mobile: String, countryCode: String): LiveData<GetOtpModel> {
        if (getOtpModel == null || getOtpModel != null) {
            getOtpModel = loginRepository.forgotPassword(activity, api, mobile, countryCode)
        }
        return getOtpModel!!
    }

    fun login(activity: Context, api: API, email: String, password: String, carrierName: String): LiveData<LoginModel> {
        if (loginModel == null || loginModel != null) {
            loginModel = loginRepository.login(activity, api, email, password, carrierName)
        }
        return loginModel!!
    }

    fun forgotPassword(context: Context, api: API, emailId: String): LiveData<ForgotPasswordModel> {
        if (forgotPasswordModel == null || forgotPasswordModel != null) {
            forgotPasswordModel = loginRepository.forgotPassword(context, api, emailId)
        }
        return forgotPasswordModel!!
    }


    fun trackSignUp(activity: Context, name: String, email: String, password: String, phone: String,
        status: String): LiveData<SignUpTrackier> {
        if (mutableLiveDataSignUpTrackier == null) {
            mutableLiveDataSignUpTrackier =
                loginRepository.trackSignUp(activity, name, email, password, phone, status)
        }
        return mutableLiveDataSignUpTrackier!!
    }

    fun trackLogin(context: Context, email: String, password: String): LiveData<LoginTrackier> {
        if (mutableLiveDataLoginTrackier == null) {
            mutableLiveDataLoginTrackier = loginRepository.trackLogin(context, email, password)
        }
        return mutableLiveDataLoginTrackier!!
    }


    fun isValidEmail(context: Context?, emailId: String): Boolean {
        if (emailId.isEmpty()) {
            DefaultHelper.showToast(context, context?.getString(R.string.ent_email))
            return false
        } else if (!DefaultHelper.isValidEmailId(emailId)) {
            DefaultHelper.showToast(context, context?.getString(R.string.invalid_email))
            return false
        }
        return true
    }

    fun isValidPassword(context: Context?, pwd: String): Boolean {
        if (pwd.isEmpty()) {
            DefaultHelper.showToast(context, context?.getString(R.string.ent_password))
            return false
        } else if (pwd.length < 6) {
            DefaultHelper.showToast(context, context?.getString(R.string.invalid_pwd_length))
            return false
        }
        return true
    }

    fun isValidTnc(context: Context?, boolean: Boolean): Boolean {
        if (!boolean) {
            DefaultHelper.showToast(context, context?.getString(R.string.kindly_agree_tnc))
            return false
        }
        return true
    }

}