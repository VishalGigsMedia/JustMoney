package com.app.just_money.terms_condition

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.faq.model.FaqModel
import com.app.just_money.my_wallet.faq.repository.FaqRepository

class TCModel : ViewModel() {

    private val faqRepository: TCRepository = TCRepository()
    private var data: LiveData<TnCModel>? = null

    fun getTC(context: Context, api: API): LiveData<TnCModel> {
        if (data == null || data != null) {
            data = faqRepository.getTC(context, api)
        }
        return data as LiveData<TnCModel>
    }
        fun getPP(context: Context, api: API): LiveData<TnCModel> {
        if (data == null || data != null) {
            data = faqRepository.getPP(context, api)
        }
        return data as LiveData<TnCModel>
    }


}