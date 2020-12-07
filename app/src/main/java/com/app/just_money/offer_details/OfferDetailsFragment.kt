package com.app.just_money.offer_details

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.R
import com.app.just_money.common_helper.BundleHelper
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentOfferDetailsBinding
import com.app.just_money.offer_details.model.OfferDetailsData
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_step_to_earn.*
import javax.inject.Inject

class OfferDetailsFragment : Fragment() {

    @Inject
    lateinit var api: API

    private var offerId: String = ""
    private var steps: String = ""
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
        getData()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        mBinding.txtStepToAvailOffer.setOnClickListener {
            showIdentityProof()
        }
        mBinding.clOfferAmount.setOnClickListener {
            claimOffer(offerId)
        }
    }

    private fun claimOffer(appId: String) {
        viewModel.claimOffer(context!!, api, appId)
            .observe(viewLifecycleOwner, { claimOfferModel ->
                if (claimOfferModel != null) {
                    if (claimOfferModel.status == DefaultKeyHelper.successCode) {
                        DefaultHelper.showToast(
                            context!!,
                            DefaultHelper.decrypt(claimOfferModel.message.toString())
                        )
                    } else {
                        DefaultHelper.showToast(
                            context!!,
                            DefaultHelper.decrypt(claimOfferModel.message.toString())
                        )
                    }
                }
            })
    }

    private fun showIdentityProof() {
        val wrappedContext =
            ContextThemeWrapper(context, R.style.ThemeOverlay_Demo_BottomSheetDialog)
        val dialog = BottomSheetDialog(wrappedContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_step_to_earn)
        dialog.window?.setGravity(Gravity.BOTTOM)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.setLayout(width, height)
        dialog.txtStepToEarnValue.text = steps
        dialog.show()
    }

    private fun getData() {
        val bundle = arguments
        if (bundle != null) {
            offerId = bundle.getString(BundleHelper.offerId).toString()
            //displayId = bundle.getString(BundleHelper.offerId).toString()
        }

        getOfferDetails()
    }

    private fun getOfferDetails() {
        viewModel.getOfferDetails(context!!, api, offerId)
            .observe(viewLifecycleOwner, { offerDetails ->
                if (offerDetails != null) {
                    when (offerDetails.status) {
                        DefaultKeyHelper.successCode -> {
                            if (offerDetails.offerDetailsData != null) {
                                setData(offerDetails.offerDetailsData)
                            }
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

    private fun setData(offerDetailsData: OfferDetailsData) {
        val offerId = DefaultHelper.decrypt(offerDetailsData.id.toString())
        val title = DefaultHelper.decrypt(offerDetailsData.name.toString())
        steps = DefaultHelper.decrypt(offerDetailsData.description.toString())
        val description = DefaultHelper.decrypt(offerDetailsData.shortDescription.toString())
        val imageUrl = DefaultHelper.decrypt(offerDetailsData.image.toString())
        val note = DefaultHelper.decrypt(offerDetailsData.note.toString())
        val actualCoins = DefaultHelper.decrypt(offerDetailsData.actualCoins.toString())
        val offerCoins = DefaultHelper.decrypt(offerDetailsData.offerCoins.toString())
        val saveCoins = offerCoins.toInt() - actualCoins.toInt()
        val saveCoinsValue = "SAVE $saveCoins"

        //println("actualCoins : $actualCoins offerCoins : $offerCoins")
        if (title.isNotEmpty()) {
            mBinding.txtTitle.text = title
        }

        if (description.isNotEmpty()) {
            mBinding.txtDescription.text = description
        }

        if (imageUrl.isNotEmpty()) {
            Glide.with(context!!)
                .load(imageUrl)
                .placeholder(R.drawable.ic_logo)
                .error(R.drawable.ic_logo)
                .into(mBinding.ivOfferImage)
        }

        if (actualCoins.isNotEmpty()) {
            mBinding.txtActualCoins.text = actualCoins
        }

        if (offerCoins.isNotEmpty()) {
            mBinding.txtOfferCoins.text = offerCoins
            val offerAmount = "EARN $offerCoins"
            mBinding.txtOfferAmount.text = offerAmount
        }

        if (saveCoinsValue.isNotEmpty()) {
            mBinding.txtSaveCoins.text = saveCoinsValue
        }

        if (note.isNotEmpty()) {
            mBinding.txtNoteValue.text = note
        }
    }


}