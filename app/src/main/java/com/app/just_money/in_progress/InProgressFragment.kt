package com.app.just_money.in_progress

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.available.AvailableOfferViewModel
import com.app.just_money.available.adapter.QuickDealsAdapter
import com.app.just_money.available.model.AvailableOffer
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentInProgressBinding
import com.app.just_money.in_progress.adapter.InProgressAdapter
import com.app.just_money.in_progress.model.PendingList
import javax.inject.Inject

class InProgressFragment : Fragment(), QuickDealsAdapter.OnClickedQuickDeals {
    @Inject
    lateinit var api: API

    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var quickDealsAdapter: QuickDealsAdapter
    private lateinit var inProgressAdapter: InProgressAdapter
    private lateinit var viewModel: InProgressViewModel
    private lateinit var viewModelAvailable: AvailableOfferViewModel
    private lateinit var mBinding: FragmentInProgressBinding
    private var onClickedQuickDeals: QuickDealsAdapter.OnClickedQuickDeals? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(true)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(InProgressViewModel::class.java)
        viewModelAvailable = ViewModelProvider(this).get(AvailableOfferViewModel::class.java)
        onClickedQuickDeals = this
        getInProgressOffers()
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun getInProgressOffers() {
        mBinding.shimmerViewContainer.startShimmer()
        viewModel.getInProgressOffers(context!!, api)
            .observe(viewLifecycleOwner, { inProgressOffers ->
                mBinding.shimmerViewContainer.stopShimmer()
                mBinding.shimmerViewContainer.visibility = GONE
                mBinding.nsv.visibility = VISIBLE
                if (inProgressOffers != null) {
                    when (inProgressOffers.status) {
                        DefaultKeyHelper.successCode -> {
                            if (inProgressOffers.data?.pendingList != null) {
                                mBinding.rvInProgressDeals.visibility = VISIBLE
                                setAdapter(inProgressOffers.data.pendingList)
                            } else {
                                mBinding.rvInProgressDeals.visibility = GONE
                            }
                            if (inProgressOffers.data?.quickDealsList != null) {
                                mBinding.txtQuickDeals.visibility = VISIBLE
                                mBinding.rvQuickDeals.visibility = VISIBLE
                                setAdapterQuickDeals(inProgressOffers.data.quickDealsList)
                            } else {
                                mBinding.txtQuickDeals.visibility = GONE
                                mBinding.rvQuickDeals.visibility = GONE
                            }
                            //if no offer available between two types, just showing error screen
                            if (mBinding.rvInProgressDeals.visibility == GONE && mBinding.rvQuickDeals.visibility == GONE) showErrorScreen()

                        }
                        DefaultKeyHelper.failureCode -> {
                            DefaultHelper.showToast(context, DefaultHelper.decrypt(inProgressOffers.message.toString()))
                            showErrorScreen()
                        }
                        DefaultKeyHelper.forceLogoutCode -> {

                        }
                        else -> {
                            DefaultHelper.showToast(context, DefaultHelper.decrypt(inProgressOffers.message.toString()))
                            showErrorScreen()
                        }

                    }
                } else {
                    showErrorScreen()
                }
            })
    }

    private fun showErrorScreen() {
        mBinding.clData.visibility = GONE
        mBinding.llError.visibility = VISIBLE
    }

    private fun setAdapter(inProgressOfferData: List<PendingList>) {

        inProgressAdapter = InProgressAdapter(activity, inProgressOfferData)
        mBinding.rvInProgressDeals.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvInProgressDeals.adapter = inProgressAdapter
        inProgressAdapter.notifyDataSetChanged()
    }

    private fun setAdapterQuickDeals(quickDeals: List<AvailableOffer>) {
        if (quickDeals.isNotEmpty()) {
            mBinding.txtQuickDeals.visibility = VISIBLE
            mBinding.rvQuickDeals.visibility = VISIBLE

            quickDealsAdapter = QuickDealsAdapter(activity!!, quickDeals, onClickedQuickDeals!!)
            mBinding.rvQuickDeals.adapter = quickDealsAdapter
            quickDealsAdapter.notifyDataSetChanged()
        } else {
            mBinding.txtQuickDeals.visibility = GONE
            mBinding.rvQuickDeals.visibility = GONE
        }
    }

    override fun getOffers(appId: String, url: String) {
        claimOffer(appId, url)
    }


    private fun claimOffer(appId: String, url: String) {
        viewModelAvailable.claimOffer(context!!, api, appId)
            .observe(viewLifecycleOwner, { claimOfferModel ->
                if (claimOfferModel != null) {
                    if (claimOfferModel.status == DefaultKeyHelper.successCode) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)
                    } else DefaultHelper.showToast(context, DefaultHelper.decrypt(claimOfferModel.message.toString()))
                }
            })
    }


}