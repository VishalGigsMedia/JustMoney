package com.app.cent4free.login.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.dagger.API
import com.app.cent4free.login.model.ForgotPasswordModel
import com.app.cent4free.login.model.GetOtpModel
import com.app.cent4free.login.model.LoginTrackier
import com.app.cent4free.login.model.SignUpTrackier
import com.app.cent4free.login.model.login.LoginModel
import com.app.cent4free.login.repository.LoginRepository

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
        return getOtpModel as LiveData<GetOtpModel>
    }

    fun login(activity: Context, api: API, login_type: String, email: String, password_or_id: String, carrierName: String): LiveData<LoginModel> {
        if (loginModel == null || loginModel != null) {
            loginModel = loginRepository.login(activity, api,login_type, email, password_or_id, carrierName)
        }
        return loginModel as LiveData<LoginModel>
    }

    fun forgotPassword(context: Context, api: API, emailId: String): LiveData<ForgotPasswordModel> {
        if (forgotPasswordModel == null || forgotPasswordModel != null) {
            forgotPasswordModel = loginRepository.forgotPassword(context, api, emailId)
        }
        return forgotPasswordModel as LiveData<ForgotPasswordModel>
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
        return mutableLiveDataLoginTrackier as LiveData<LoginTrackier>
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
            showToast(context, context?.getString(R.string.kindly_agree_tnc))
            return false
        }
        return true
    }

}