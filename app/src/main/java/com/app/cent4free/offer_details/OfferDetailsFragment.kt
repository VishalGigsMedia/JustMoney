package com.app.cent4free.offer_details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.cent4free.MainActivity
import com.app.cent4free.R
import com.app.cent4free.available.AvailableOfferViewModel
import com.app.cent4free.common_helper.BundleHelper
import com.app.cent4free.common_helper.DefaultHelper
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.common_helper.DefaultKeyHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.MyApplication
import com.app.cent4free.databinding.FragmentOfferDetailsBinding
import com.app.cent4free.my_wallet.faq.FaqFragment
import com.app.cent4free.offer_details.adapter.StepsAdapter
import com.app.cent4free.offer_details.model.OfferDetailsData
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_step_to_earn.*
import javax.inject.Inject

class OfferDetailsFragment : Fragment() {

    @Inject
    lateinit var api: API

    private var offerId: String = ""
    private var offerTrackierId: String = ""
    private var source: String = ""
    private var steps: String = ""
    private var stepsDescription: ArrayList<String>? = null
    private lateinit var viewModel: OfferDetailsViewModel
    private lateinit var viewModelAO: AvailableOfferViewModel
    private lateinit var mBinding: FragmentOfferDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_details, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(OfferDetailsViewModel::class.java)
        viewModelAO = ViewModelProvider(this).get(AvailableOfferViewModel::class.java)
        offerTrackierId = arguments?.getString(BundleHelper.offer_trackier_id).toString()
        source = arguments?.getString(BundleHelper.source).toString()
        Log.d("jgvdhjbjkn", "offer_id: $offerId , trackier_id: $offerTrackierId")

        getOfferDetails()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        mBinding.txtStepToAvailOffer.setOnClickListener {
            showDialogue()
        }
        mBinding.txtTitle.setOnClickListener {
            activity?.onBackPressed()
        }
        mBinding.txtHaveAQuestion.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, FaqFragment())
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        }
    }

    private fun claimOffer(offer_id: String, url: String) {
        viewModelAO.claimOffer(context!!, api, offer_id).observe(viewLifecycleOwner, { claimOfferModel ->
            if (claimOfferModel != null) {
                if (claimOfferModel.status == DefaultKeyHelper.successCode) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)

                    mBinding.txtOfferAmount.text = getString(R.string.open)
                } else showToast(context!!, decrypt(claimOfferModel.message.toString()))
            }
        })
    }

    private fun showDialogue() {
        val dialog = BottomSheetDialog(context!!, R.style.AppBottomSheetDialogTheme)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_step_to_earn)
        dialog.window?.setGravity(Gravity.BOTTOM)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.txtStepToEarnValue.text = steps
        dialog.rvSteps.adapter = stepsDescription?.let { StepsAdapter(context, it) }
        dialog.show()
    }

    private fun getOfferDetails() {
        mBinding.shimmer.startShimmer()
        viewModel.getOfferDetails(context, api, offerTrackierId).observe(viewLifecycleOwner, { offerDetails ->
            mBinding.shimmer.stopShimmer()
            mBinding.shimmer.visibility = GONE
            mBinding.nsv.visibility = VISIBLE
            if (offerDetails != null) {
                when (offerDetails.status) {
                    DefaultKeyHelper.successCode -> if (offerDetails.data != null) {
                        setData(offerDetails.data)
                    }
                    DefaultKeyHelper.failureCode -> showErrorScreen()
                    DefaultKeyHelper.forceLogoutCode -> DefaultHelper.forceLogout(activity)
                    else -> showErrorScreen()
                }
            } else showErrorScreen()
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setData(offerDetailsData: OfferDetailsData) {
        val url = decrypt(offerDetailsData.url)
        val title = decrypt(offerDetailsData.name)
        steps = decrypt(offerDetailsData.description)
        if (offerDetailsData.steps_description != null) {
            stepsDescription = offerDetailsData.steps_description as ArrayList<String>
            mBinding.clStepToAvailOffer.visibility = VISIBLE
        } else mBinding.clStepToAvailOffer.visibility = GONE
        val description = decrypt(offerDetailsData.short_description)
        val imageUrl = decrypt(offerDetailsData.image)
        val note = decrypt(offerDetailsData.note)
        val actualCoins = decrypt(offerDetailsData.actual_points)
        val offerCoins = decrypt(offerDetailsData.offer_points)
        val saveCoins = offerCoins.toInt() - actualCoins.toInt()
        val saveCoinsValue = "Earn Extra $saveCoins"
        offerId = offerDetailsData.id

        if (title.isNotEmpty()) mBinding.txtTitle.text = "  $title"
        if (description.isNotEmpty()) mBinding.txtDescription.text = description
        if (imageUrl.isNotEmpty()) {
            Glide.with(context!!).load(imageUrl).placeholder(R.drawable.logo_without_text).error(R.drawable.logo_without_text)
                .into(mBinding.ivOfferImage)
        }
        if (actualCoins.isNotEmpty()) mBinding.txtActualCoins.text = actualCoins
        if (offerCoins.isNotEmpty()) {
            mBinding.txtOfferCoins.text = offerCoins
            if (source == BundleHelper.inProgress) mBinding.txtOfferAmount.text = getString(R.string.open)
            else mBinding.txtOfferAmount.text = "EARN $offerCoins"
        }
        if (saveCoinsValue.isNotEmpty()) mBinding.txtSaveCoins.text = saveCoinsValue
        if (note.isNotEmpty()) mBinding.txtNoteValue.text = note

        mBinding.clOfferAmount.setOnClickListener {
            if (!mBinding.txtOfferAmount.text.contains("EARN")) {
                if (url.contains("http")) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            } else {
                claimOffer(offerId, url)
            }
        }
    }

    private fun showErrorScreen() {
        mBinding.clData.visibility = GONE
        mBinding.llError.visibility = VISIBLE
    }


}