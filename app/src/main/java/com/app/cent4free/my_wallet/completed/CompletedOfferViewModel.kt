package com.app.cent4free.my_wallet.completed

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.dagger.API
import com.app.cent4free.my_wallet.completed.model.CompletedOfferModel

class CompletedOfferViewModel : ViewModel() {
    private val completedOfferRepository: CompletedOfferRepository = CompletedOfferRepository()
    private var completedOfferModel: LiveData<CompletedOfferModel>? = null

    fun getCompletedOffers(context: Context?, api: API, scroll_offset: Int, scroll_limit: Int): LiveData<CompletedOfferModel> {
        if (completedOfferModel != null || completedOfferModel == null) {
            completedOfferModel = completedOfferRepository.getCompletedOffers(context, api,scroll_offset,scroll_limit)
        }
        return completedOfferModel as LiveData<CompletedOfferModel>
    }
}