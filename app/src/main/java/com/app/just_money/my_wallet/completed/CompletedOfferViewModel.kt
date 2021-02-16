package com.app.just_money.my_wallet.completed

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.my_wallet.completed.model.CompletedOfferModel

class CompletedOfferViewModel : ViewModel() {
    private val completedOfferRepository: CompletedOfferRepository = CompletedOfferRepository()
    private var completedOfferModel: LiveData<CompletedOfferModel>? = null

    fun getCompletedOffers(context: Context?, api: API): LiveData<CompletedOfferModel> {
        if (completedOfferModel != null || completedOfferModel == null) {
            completedOfferModel = completedOfferRepository.getCompletedOffers(context, api)
        }
        return completedOfferModel as LiveData<CompletedOfferModel>
    }
}