package com.app.just_money.my_wallet.setting

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.setting.model.SendFeedbackModel
import com.app.just_money.my_wallet.setting.repository.HelpRepository
import java.io.File

class HelpViewModel : ViewModel() {

    private val helpRepository: HelpRepository = HelpRepository()
    private var sendFeedbackModel: LiveData<SendFeedbackModel>? = null

    fun sendFeedback(context: Context, api: API, name: String, email: String, subject: String, message: String,
        uploadImage: File?): LiveData<SendFeedbackModel> {
        if (sendFeedbackModel == null || sendFeedbackModel != null) {
            sendFeedbackModel =
                helpRepository.sendFeedback(context, api, name, email, subject, message, uploadImage)
        }
        return sendFeedbackModel as LiveData<SendFeedbackModel>
    }

}