package com.app.just_money.my_wallet.completed

import android.os.Bundle
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
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentCompletedBinding
import com.app.just_money.my_wallet.completed.adapter.CompletedAdapter
import javax.inject.Inject

class CompletedFragment : Fragment() {
    @Inject
    lateinit var api: API

    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var completedAdapter: CompletedAdapter
    private lateinit var viewModel: CompletedOfferViewModel
    private lateinit var mBinding: FragmentCompletedBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(false)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(CompletedOfferViewModel::class.java)
        getCompletedOffer()
        manageClicks()
    }

    private fun manageClicks() {
        mBinding.txtCompleted.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun getCompletedOffer() {
        mBinding.shimmer.startShimmer()
        viewModel.getCompletedOffers(context, api).observe(viewLifecycleOwner, { completedOffers ->
            mBinding.shimmer.stopShimmer()
            mBinding.shimmer.visibility = GONE
            mBinding.nsv.visibility = VISIBLE
            if (completedOffers != null) {
                when (completedOffers.status) {
                    DefaultKeyHelper.successCode -> {
                        if (completedOffers.data?.completedList != null && completedOffers.data.completedList.isNotEmpty()) {
                            mBinding.rvCompleted.visibility = VISIBLE
                            mBinding.rvCompleted.adapter = CompletedAdapter(activity!!, completedOffers.data.completedList)
                        } else showErrorScreen()
                    }
                    DefaultKeyHelper.failureCode -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(completedOffers.message.toString()))
                        showErrorScreen()
                    }
                    DefaultKeyHelper.forceLogoutCode -> { }
                    else -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(completedOffers.message.toString()))
                        showErrorScreen()
                    }
                }
            }else showErrorScreen()
        })
    }

    private fun showErrorScreen() {
        mBinding.rvCompleted.visibility = GONE
        mBinding.llError.visibility = VISIBLE
    }

}