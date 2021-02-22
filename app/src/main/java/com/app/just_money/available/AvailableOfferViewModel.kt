package com.app.just_money.available

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.just_money.available.model.AvailableOfferModel
import com.app.just_money.available.model.IpAddressModel
import com.app.just_money.available.repository.AvailableOfferRepository
import com.app.just_money.dagger.API
import com.app.just_money.login.model.ClaimOfferModel
import com.app.just_money.model.CheckAppVersionModel

class AvailableOfferViewModel : ViewModel() {

    private val availableOfferRepository: AvailableOfferRepository = AvailableOfferRepository()
    private var availableOfferModel: LiveData<AvailableOfferModel>? = null
    private var claimOfferModel: LiveData<ClaimOfferModel>? = null
    private var versionModel: LiveData<CheckAppVersionModel>? = null
    private var ipAddressModel: LiveData<IpAddressModel>? = null

    fun getOffers(context: Context, api: API): LiveData<AvailableOfferModel> {
        if (availableOfferModel != null || availableOfferModel == null) {
            availableOfferModel = availableOfferRepository.getOffers(context, api)
        }
        return availableOfferModel as LiveData<AvailableOfferModel>
    }

    fun claimOffer(context: Context, api: API, appId: String): LiveData<ClaimOfferModel> {
        if (claimOfferModel != null || claimOfferModel == null) {
            claimOfferModel = availableOfferRepository.claimOffer(context, api, appId)
        }
        return claimOfferModel as LiveData<ClaimOfferModel>
    }


    fun checkVersion(context: Context, api: API): LiveData<CheckAppVersionModel> {
        if (versionModel != null || versionModel == null) {
            versionModel = availableOfferRepository.checkVersion(context, api)
        }
        return versionModel as LiveData<CheckAppVersionModel>
    }

    fun getIPAddress(context: Context, api: API): LiveData<IpAddressModel> {
        ipAddressModel = availableOfferRepository.getIPAddress(context, api)
        return ipAddressModel as LiveData<IpAddressModel>
    }

}