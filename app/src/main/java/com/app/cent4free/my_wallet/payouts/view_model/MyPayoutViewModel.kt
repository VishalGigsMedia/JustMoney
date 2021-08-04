package com.app.cent4free.my_wallet.payouts.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.dagger.API
import com.app.cent4free.my_wallet.payouts.PayoutHistoryRepository
import com.app.cent4free.my_wallet.payouts.model.PayoutModel

class MyPayoutViewModel : ViewModel() {
    private val payoutHistoryRepository: PayoutHistoryRepository = PayoutHistoryRepository()
    private var payoutHistoryModel: LiveData<PayoutModel>? = null

    fun getPayoutHistory(context: Context?, api: API,offset:Int,nextLimit: Int): LiveData<PayoutModel> {
        if (payoutHistoryModel != null || payoutHistoryModel == null) {
            payoutHistoryModel = payoutHistoryRepository.getPayoutHistory(context, api,offset,nextLimit)
        }
        return payoutHistoryModel as LiveData<PayoutModel>
    }

}