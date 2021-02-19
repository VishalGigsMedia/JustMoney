package com.app.just_money.offer_details

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.BundleHelper
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentOfferDetailsBinding
import com.app.just_money.my_wallet.faq.FaqFragment
import com.app.just_money.offer_details.adapter.StepsAdapter
import com.app.just_money.offer_details.model.OfferDetailsData
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_step_to_earn.*
import javax.inject.Inject

class OfferDetailsFragment : Fragment() {

    @Inject
    lateinit var api: API

    private var offerId: String = ""
    private var source: String = ""
    private var steps: String = ""
    private lateinit var stepsDescription: ArrayList<String>
    private lateinit var viewModel: OfferDetailsViewModel
    private lateinit var mBinding: FragmentOfferDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_offer_details, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(OfferDetailsViewModel::class.java)
        offerId = arguments?.getString(BundleHelper.offerId).toString()
        source = arguments?.getString(BundleHelper.source).toString()
        //displayId = bundle.getString(BundleHelper.offerId).toString()

        getOfferDetails()
        setOnClickListener()
    }

    private fun setOnClickListener() {
        mBinding.txtStepToAvailOffer.setOnClickListener {
            showIdentityProof()
        }
        mBinding.txtTitle.setOnClickListener {
            activity?.onBackPressed()
        }
        mBinding.txtHaveAQuestion.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, FaqFragment())
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        }
    }

    private fun claimOffer(appId: String, url: String) {
        viewModel.claimOffer(context!!, api, appId).observe(viewLifecycleOwner, { claimOfferModel ->
            if (claimOfferModel != null) {
                if (claimOfferModel.status == DefaultKeyHelper.successCode) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)

                    mBinding.txtOfferAmount.text = getString(R.string.open)
                } else DefaultHelper.showToast(context!!, DefaultHelper.decrypt(claimOfferModel.message.toString()))
            }
        })
    }

    private fun showIdentityProof() {
        val dialog = BottomSheetDialog(context!!, R.style.AppBottomSheetDialogTheme)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_step_to_earn)
        dialog.window?.setGravity(Gravity.BOTTOM)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.txtStepToEarnValue.text = steps
        dialog.rvSteps.adapter = StepsAdapter(context, stepsDescription)
        dialog.show()
    }

    private fun getOfferDetails() {
        mBinding.shimmer.startShimmer()
        viewModel.getOfferDetails(context, api, offerId).observe(viewLifecycleOwner, { offerDetails ->
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
        //val offerId = DefaultHelper.decrypt(offerDetailsData.id.toString())
        val url = DefaultHelper.decrypt(offerDetailsData.url)
        val title = DefaultHelper.decrypt(offerDetailsData.name)
        steps = DefaultHelper.decrypt(offerDetailsData.description)
        stepsDescription = offerDetailsData.steps_description as ArrayList<String>
        val description = DefaultHelper.decrypt(offerDetailsData.short_description)
        val imageUrl = DefaultHelper.decrypt(offerDetailsData.image)
        val note = DefaultHelper.decrypt(offerDetailsData.note)
        val actualCoins = DefaultHelper.decrypt(offerDetailsData.actual_coins)
        val offerCoins = DefaultHelper.decrypt(offerDetailsData.offer_coins)
        val saveCoins = offerCoins.toInt() - actualCoins.toInt()
        val saveCoinsValue = "Earn Extra $saveCoins"

        if (title.isNotEmpty()) mBinding.txtTitle.text = "  $title"
        if (description.isNotEmpty()) mBinding.txtDescription.text = description
        if (imageUrl.isNotEmpty()) {
            Glide.with(context!!).load(imageUrl).placeholder(R.drawable.ic_logo).error(R.drawable.ic_logo)
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
            /*if (source == BundleHelper.inProgress) {
                if (url.contains("http")) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            } else claimOffer(offerId, url)*/
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
        mBinding.clData.visibility = View.GONE
        mBinding.llError.visibility = View.VISIBLE
    }


}