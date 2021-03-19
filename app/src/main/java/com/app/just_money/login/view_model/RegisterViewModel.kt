package com.app.just_money.login.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultHelper.showToast
import com.app.just_money.dagger.API
import com.app.just_money.login.model.RegisterUserModel
import com.app.just_money.login.repository.RegisterRepository

class RegisterViewModel : ViewModel() {
    private var registerRepository: RegisterRepository = RegisterRepository()
    private var registerUserModel: LiveData<RegisterUserModel>? = null

    fun register(activity: Context?, api: API, firstName: String, lastName: String, emailId: String,
        password: String, refCode: String): LiveData<RegisterUserModel> {
        if (registerUserModel == null || registerUserModel != null) {
            registerUserModel = registerRepository.register(activity, api, firstName, lastName, emailId, password,refCode)
        }
        return registerUserModel as LiveData<RegisterUserModel>
    }


    fun isValidFirstName(context: Context?, firstName: String): Boolean {
        if (firstName.isEmpty()) {
            showToast(context, context?.getString(R.string.ent_first_name))
            return false
        }
        return true
    }

    fun isValidLastName(context: Context?, firstName: String): Boolean {
        return if (firstName.isEmpty()) {
            showToast(context, context?.getString(R.string.ent_last_name))
            false
        } else true
    }


    fun isValidEmail(context: Context?, emailId: String): Boolean {
        return if (emailId.isEmpty()) {
            showToast(context, context?.getString(R.string.ent_email))
            false
        } else if (!DefaultHelper.isValidEmailId(emailId)) {
            showToast(context, context?.getString(R.string.invalid_email))
            false
        } else true
    }


    fun isValidPassword(context: Context?, password: String, confirmPassword: String): Boolean {
        return when {
            password.isEmpty() -> {
                showToast(context, context?.getString(R.string.ent_password))
                false
            }
            password.length < 6 -> {
                showToast(context, context?.getString(R.string.invalid_pwd_length))
                false
            }
            confirmPassword.isEmpty() -> {
                showToast(context, context?.getString(R.string.ent_confirm_password))
                false
            }
            confirmPassword.length < 6 -> {
                showToast(context, context?.getString(R.string.invalid_confirm_pwd_length))
                false
            }
            password != confirmPassword -> {
                showToast(context, context?.getString(R.string.pwd_does_not_match))
                false
            }
            else -> true
        }
    }

    fun isValidRefCode(context: Context?, refCode: String): Boolean {
        return when {
            refCode.isEmpty() -> true
            refCode.length == 8 -> true
            else -> {
                showToast(context, "Referral Code is Invalid.")
                false
            }
        }
    }
}