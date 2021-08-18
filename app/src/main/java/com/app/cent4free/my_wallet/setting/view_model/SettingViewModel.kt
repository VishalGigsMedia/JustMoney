package com.app.cent4free.my_wallet.setting.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.dagger.API
import com.app.cent4free.my_wallet.setting.model.LogoutModel
import com.app.cent4free.my_wallet.setting.repository.SettingRepository

class SettingViewModel : ViewModel() {

    private var settingRepository: SettingRepository = SettingRepository()
    private var logoutModel: LiveData<LogoutModel>? = null

    fun logout(context: Context?, api: API): LiveData<LogoutModel> {
        if (logoutModel != null || logoutModel == null) {
            logoutModel = settingRepository.logout(context, api)
        }
        return logoutModel as LiveData<LogoutModel>
    }

    fun changePassword(context: Context?, api: API, oldPassword: String, newPassword: String): LiveData<LogoutModel> {
        if (logoutModel != null || logoutModel == null) {
            logoutModel = settingRepository.changePassword(context, api,oldPassword,newPassword)
        }
        return logoutModel as LiveData<LogoutModel>
    }

    fun isValidPassword(context: Context?, oldPassword: String, password: String, confirmPassword: String): Boolean {
        return when {
            oldPassword.isEmpty() -> {
                showToast(context, context?.getString(R.string.ent_old_password))
                false
            }
            password.isEmpty() -> {
                showToast(context, context?.getString(R.string.ent_new_password))
                false
            }
            oldPassword.length < 6 -> {
                showToast(context, context?.getString(R.string.invalid_old_pwd_length))
                false
            }
            password.length < 6 -> {
                showToast(context, context?.getString(R.string.invalid_new_pwd_length))
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
            oldPassword == password -> {
                showToast(context, context?.getString(R.string.pwd_cannot_not_match))
                false
            }
            else -> true
        }
    }

}