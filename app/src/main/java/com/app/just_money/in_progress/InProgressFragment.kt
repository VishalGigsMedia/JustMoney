package com.app.just_money.in_progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.just_money.MainActivity
import com.app.just_money.R
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
    private lateinit var mBinding: FragmentInProgressBinding
    private var onClickedQuickDeals: QuickDealsAdapter.OnClickedQuickDeals? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_in_progress, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(true)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(InProgressViewModel::class.java)
        onClickedQuickDeals = this
        getInProgressOffers()
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun getInProgressOffers() {
        viewModel.getInProgressOffers(context!!, api)
            .observe(viewLifecycleOwner, { inProgressOffers ->
                if (inProgressOffers != null) {
                    when (inProgressOffers.status) {
                        DefaultKeyHelper.successCode -> {
                            // println("quickDealsSize:" + inProgressOffers.data?.pendingList?.size + "")
                            //  println("quickDealsSize:" + inProgressOffers.data?.quickDealsList?.size + "")
                            if (inProgressOffers.data?.pendingList != null) {
                                mBinding.rvInProgressDeals.visibility = View.VISIBLE
                                setAdapter(inProgressOffers.data.pendingList)
                            } else {
                                mBinding.rvInProgressDeals.visibility = View.GONE
                            }
                            if (inProgressOffers.data?.quickDealsList != null) {
                                mBinding.txtQuickDeals.visibility = View.VISIBLE
                                mBinding.rvQuickDeals.visibility = View.VISIBLE
                                setAdapterQuickDeals(inProgressOffers.data.quickDealsList)
                            } else {
                                mBinding.txtQuickDeals.visibility = View.GONE
                                mBinding.rvQuickDeals.visibility = View.GONE
                            }
                        }
                        DefaultKeyHelper.failureCode -> {
                            DefaultHelper.showToast(
                                context!!,
                                DefaultHelper.decrypt(inProgressOffers.message.toString())
                            )
                        }
                        DefaultKeyHelper.forceLogoutCode -> {

                        }
                        else -> {
                            DefaultHelper.showToast(
                                context!!,
                                DefaultHelper.decrypt(inProgressOffers.message.toString())
                            )
                        }

                    }
                }
            })
    }

    private fun setAdapter(inProgressOfferData: List<PendingList>) {

        inProgressAdapter = InProgressAdapter(activity!!, inProgressOfferData)
        mBinding.rvInProgressDeals.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvInProgressDeals.adapter = inProgressAdapter
        inProgressAdapter.notifyDataSetChanged()
    }

    private fun setAdapterQuickDeals(quickDeals: List<AvailableOffer>) {
        if (quickDeals.isNotEmpty()) {
            mBinding.txtQuickDeals.visibility = View.VISIBLE
            mBinding.rvQuickDeals.visibility = View.VISIBLE

            quickDealsAdapter = QuickDealsAdapter(activity!!, quickDeals, onClickedQuickDeals!!)
            mBinding.rvQuickDeals.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            mBinding.rvQuickDeals.adapter = quickDealsAdapter
            quickDealsAdapter.notifyDataSetChanged()
        } else {
            mBinding.txtQuickDeals.visibility = View.GONE
            mBinding.rvQuickDeals.visibility = View.GONE
        }
    }

    override fun getOffers(appId: String, url: String) {
        //  claimOffer(appId)
    }

    /* private fun claimOffer(appId: String) {
         viewModel.claimOffer(context!!, api, appId)
             .observe(viewLifecycleOwner, Observer { claimOfferModel ->
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
     }*/

}