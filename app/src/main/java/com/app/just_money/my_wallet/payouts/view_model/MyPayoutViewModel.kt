package com.app.just_money.my_wallet.payouts.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.payouts.PayoutHistoryRepository
import com.app.just_money.my_wallet.payouts.model.PayoutHistoryModel

class MyPayoutViewModel : ViewModel() {
    private val payoutHistoryRepository: PayoutHistoryRepository = PayoutHistoryRepository()
    private var payoutHistoryModel: LiveData<PayoutHistoryModel>? = null

    fun getPayoutHistory(context: Context?, api: API): LiveData<PayoutHistoryModel> {
        if (payoutHistoryModel != null || payoutHistoryModel == null) {
            payoutHistoryModel = payoutHistoryRepository.getPayoutHistory(context, api)
        }
        return payoutHistoryModel as LiveData<PayoutHistoryModel>
    }

}