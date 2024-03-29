package com.app.cent4free.offer_details

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.dagger.API
import com.app.cent4free.login.model.ClaimOfferModel
import com.app.cent4free.offer_details.model.OfferDetailsModel
import com.app.cent4free.offer_details.repository.OfferDetailsRepository

class OfferDetailsViewModel : ViewModel() {

    private val offerDetailRepository: OfferDetailsRepository = OfferDetailsRepository()
    private var offerDetailModel: LiveData<OfferDetailsModel>? = null
    private var claimOfferModel: LiveData<ClaimOfferModel>? = null

    fun getOfferDetails(context: Context?, api: API, offer_trackier_id: String): LiveData<OfferDetailsModel> {
        if (offerDetailModel == null || offerDetailModel != null) {
            offerDetailModel = offerDetailRepository.getOfferDetails(context, api, offer_trackier_id)
        }
        return offerDetailModel as LiveData<OfferDetailsModel>
    }

}