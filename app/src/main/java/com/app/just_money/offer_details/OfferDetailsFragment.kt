package com.app.just_money.offer_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.R
import com.app.just_money.common_helper.BundleHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentOfferDetailsBinding
import javax.inject.Inject

class OfferDetailsFragment : Fragment() {

    @Inject
    lateinit var api: API

    private var offerId: String = ""
    private var displayId: String = ""
    private lateinit var viewModel: OfferDetailsViewModel
    private lateinit var mBinding: FragmentOfferDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_offer_details, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(OfferDetailsViewModel::class.java)
        getData();
    }

    private fun getData() {
        val bundle = arguments
        if (bundle != null) {
            offerId = bundle.getString(BundleHelper.offerId).toString()
            //displayId = bundle.getString(BundleHelper.offerId).toString()
        }

        viewModel.getOfferDetails(context!!, api, offerId)
            .observe(viewLifecycleOwner, { offerDetails ->
                if (offerDetails != null) {
                    when (offerDetails.status) {
                        DefaultKeyHelper.successCode -> {

                        }
                        DefaultKeyHelper.failureCode -> {

                        }
                        DefaultKeyHelper.forceLogoutCode -> {

                        }
                        else -> {

                        }
                    }
                }
            })
    }


}