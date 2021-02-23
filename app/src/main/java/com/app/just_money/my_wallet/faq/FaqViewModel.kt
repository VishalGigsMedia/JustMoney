package com.app.just_money.my_wallet.faq

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.faq.model.FaqModel
import com.app.just_money.my_wallet.faq.repository.FaqRepository

class FaqViewModel : ViewModel() {

    private val faqRepository: FaqRepository = FaqRepository()
    private var faqDetails: LiveData<FaqModel>? = null

    fun getFaq(context: Context, api: API): LiveData<FaqModel> {
        if (faqDetails == null || faqDetails != null) {
            faqDetails = faqRepository.getFaq(context, api)
        }
        return faqDetails as LiveData<FaqModel>
    }


}