package com.app.just_money.terms_condition

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.faq.model.FaqModel
import com.app.just_money.my_wallet.faq.repository.FaqRepository

class TCModel : ViewModel() {

    private val faqRepository: TCRepository = TCRepository()
    private var faqDetails: LiveData<FaqModel>? = null

    fun getTC(context: Context, api: API): LiveData<FaqModel> {
        if (faqDetails == null || faqDetails != null) {
            faqDetails = faqRepository.getTC(context, api)
        }
        return faqDetails as LiveData<FaqModel>
    }
        fun getPP(context: Context, api: API): LiveData<FaqModel> {
        if (faqDetails == null || faqDetails != null) {
            faqDetails = faqRepository.getPP(context, api)
        }
        return faqDetails as LiveData<FaqModel>
    }


}