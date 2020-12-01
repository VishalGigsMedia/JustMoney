package com.app.just_money.offer_details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.dagger.API
import com.app.just_money.offer_details.model.OfferDetailsModel
import com.app.just_money.offer_details.repository.OfferDetailsRepository

class OfferDetailsViewModel : ViewModel() {

    private val offerDetailRepository: OfferDetailsRepository = OfferDetailsRepository()
    private var offerDetailModel: LiveData<OfferDetailsModel>? = null

    fun getOfferDetails(
        context: Context,
        api: API,
        offerId: String
    ): LiveData<OfferDetailsModel> {
        if (offerDetailModel == null || offerDetailModel != null) {
            offerDetailModel = offerDetailRepository.getOfferDetails(context, api, offerId)
        }
        return offerDetailModel!!
    }


}