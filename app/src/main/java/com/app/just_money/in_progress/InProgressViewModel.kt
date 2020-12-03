package com.app.just_money.in_progress

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.in_progress.model.InProgressModel

class InProgressViewModel : ViewModel() {
    private val inProgressOfferRepository: InProgressOfferRepository = InProgressOfferRepository()
    private var inProgressOfferModel: LiveData<InProgressModel>? = null

    fun getInProgressOffers(
        context: Context,
        api: API
    ): LiveData<InProgressModel> {
        if (inProgressOfferModel != null || inProgressOfferModel == null) {
            inProgressOfferModel = inProgressOfferRepository.getInProgressOffers(context, api)
        }
        return inProgressOfferModel!!
    }
}