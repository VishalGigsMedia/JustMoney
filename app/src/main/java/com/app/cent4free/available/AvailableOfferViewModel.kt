package com.app.cent4free.available

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.cent4free.available.model.AvailableOfferModel
import com.app.cent4free.available.model.IpApiModel
import com.app.cent4free.available.repository.AvailableOfferRepository
import com.app.cent4free.dagger.API
import com.app.cent4free.login.model.ClaimOfferModel
import com.app.cent4free.model.CheckAppVersionModel
import com.app.cent4free.my_wallet.model.EarningsModel

class AvailableOfferViewModel : ViewModel() {

    private val availableOfferRepository: AvailableOfferRepository = AvailableOfferRepository()
    private var availableOfferModel: LiveData<AvailableOfferModel>? = null
    private var claimOfferModel: LiveData<ClaimOfferModel>? = null
    private var earningsModel: LiveData<EarningsModel>? = null
    private var versionModel: LiveData<CheckAppVersionModel>? = null
    private var ipAddressModel: LiveData<IpApiModel>? = null

    fun getOffers(context: Context?, api: API): LiveData<AvailableOfferModel> {
        if (availableOfferModel != null || availableOfferModel == null) {
            availableOfferModel = availableOfferRepository.getOffers(context, api)
        }
        return availableOfferModel as LiveData<AvailableOfferModel>
    }

    fun claimOffer(context: Context?, api: API, offer_id: String): LiveData<ClaimOfferModel> {
        if (claimOfferModel != null || claimOfferModel == null) {
            claimOfferModel = availableOfferRepository.claimOffer(context, api, offer_id)
        }
        return claimOfferModel as LiveData<ClaimOfferModel>
    }
    fun claimReward(context: Context?, api: API): LiveData<ClaimOfferModel> {
        claimOfferModel = availableOfferRepository.claimReward(context, api)
        return claimOfferModel as LiveData<ClaimOfferModel>
    }
    fun getEarnings(context: Context?, api: API): LiveData<EarningsModel> {
        earningsModel = availableOfferRepository.getEarnings(context, api)
        return earningsModel as LiveData<EarningsModel>
    }
    fun requestPayout(context: Context?, api: API, amt:String): LiveData<EarningsModel> {
        earningsModel = availableOfferRepository.requestPayout(context, api,amt)
        return earningsModel as LiveData<EarningsModel>
    }

    fun getIPAddress(context: Context?, api: API): LiveData<IpApiModel> {
        return  availableOfferRepository.getIPAddress(context, api)
    }

}