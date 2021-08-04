package com.app.cent4free.my_wallet.setting.help

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.dagger.API
import com.app.cent4free.my_wallet.setting.model.SendFeedbackModel
import com.app.cent4free.my_wallet.setting.repository.HelpRepository
import java.io.File
import java.util.ArrayList

class HelpViewModel : ViewModel() {

    private val helpRepository: HelpRepository = HelpRepository()
    private var sendFeedbackModel: LiveData<SendFeedbackModel>? = null

    fun sendFeedback(context: Context, api: API, name: String, email: String, subject: String, message: String,
        uploadImage: ArrayList<File>): LiveData<SendFeedbackModel> {
        if (sendFeedbackModel == null || sendFeedbackModel != null) {
            sendFeedbackModel =
                helpRepository.sendFeedback(context, api, name, email, subject, message, uploadImage)
        }
        return sendFeedbackModel as LiveData<SendFeedbackModel>
    }

}