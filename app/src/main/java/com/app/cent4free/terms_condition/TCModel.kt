package com.app.cent4free.terms_condition

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.dagger.API

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