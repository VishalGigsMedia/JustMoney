package com.app.just_money.login.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.dagger.API
import com.app.just_money.login.model.RegisterUserModel
import com.app.just_money.login.repository.RegisterRepository

class RegisterViewModel : ViewModel() {
    private var registerRepository: RegisterRepository = RegisterRepository()
    private var registerUserModel: LiveData<RegisterUserModel>? = null

    fun register(activity: Context?, api: API, firstName: String, lastName: String, emailId: String, password: String): LiveData<RegisterUserModel> {
        if (registerUserModel == null || registerUserModel != null) {
            registerUserModel = registerRepository.register(activity, api, firstName, lastName, emailId, password)
        }
        return registerUserModel!!
    }


    fun isValid(context: Context?, firstName: String, lastName: String, emailId: String, password: String, confirmPassword: String) {

        if (!isValidFirstName(context, firstName)) {
            return
        }

        if (!isValidLastName(context, lastName)) {
            return
        }

        if (!isValidEmail(context, emailId)) {
            return
        }

        if (!isValidPassword(context, password, confirmPassword)) {
            return
        }


    }

    fun isValidFirstName(context: Context?, firstName: String): Boolean {
        if (firstName.isEmpty()) {
            DefaultHelper.showToast(context, context?.getString(R.string.ent_first_name))
            return false
        }
        return true
    }

    fun isValidLastName(context: Context?, firstName: String): Boolean {
        if (firstName.isEmpty()) {
            DefaultHelper.showToast(context, context?.getString(R.string.ent_last_name))
            return false
        }
        return true
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


    fun isValidPassword(context: Context?, password: String, confirmPassword: String): Boolean {
        return when {
            password.isEmpty() -> {
                DefaultHelper.showToast(context, context?.getString(R.string.ent_password))
                false
            }
            confirmPassword.isEmpty() -> {
                DefaultHelper.showToast(context, context?.getString(R.string.ent_confirm_password))
                false
            }
            password != confirmPassword -> {
                DefaultHelper.showToast(context, context?.getString(R.string.pwd_does_not_match))
                false
            }
            else -> true
        }
    }

}