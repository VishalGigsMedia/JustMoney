package com.app.just_money.my_wallet.setting.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.setting.model.LogoutModel
import com.app.just_money.my_wallet.setting.repository.SettingRepository

class SettingViewModel : ViewModel() {

    private var settingRepository: SettingRepository = SettingRepository()
    private var logoutModel: LiveData<LogoutModel>? = null

    fun logout(context: Context?, api: API): LiveData<LogoutModel> {
        if (logoutModel != null || logoutModel == null) {
            logoutModel = settingRepository.logout(context, api)
        }
        return logoutModel as LiveData<LogoutModel>
    }

}