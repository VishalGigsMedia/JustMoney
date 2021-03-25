package com.app.just_money.my_wallet.payouts

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
import com.app.just_money.common_helper.DefaultHelper.decrypt
import com.app.just_money.common_helper.DefaultHelper.showToast
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentMyPayoutBinding
import com.app.just_money.my_wallet.payouts.adapter.HistoryAdapter
import com.app.just_money.my_wallet.payouts.view_model.MyPayoutViewModel
import javax.inject.Inject

class MyPayoutFragment : Fragment() {
    @Inject
    lateinit var api: API
    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var viewModel: MyPayoutViewModel
    private lateinit var mBinding: FragmentMyPayoutBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_payout, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        manageClicks()
        getPayoutHistory()
    }

    private fun manageClicks() {
        mBinding.txtTitle.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    private fun init() {
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(MyPayoutViewModel::class.java)
        callback?.onShowHideBottomNav(false)
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun getPayoutHistory() {
        mBinding.shimmer.startShimmer()
        viewModel.getPayoutHistory(context, api).observe(viewLifecycleOwner, { payoutHistoryModel ->
            mBinding.shimmer.stopShimmer()
            mBinding.shimmer.visibility = GONE
            mBinding.nsv.visibility = VISIBLE
            if (payoutHistoryModel != null) when (payoutHistoryModel.status) {
                DefaultKeyHelper.successCode -> {
                    if (decrypt(payoutHistoryModel.data?.total_coins.toString()).isNotEmpty()) {
                        mBinding.txtBalanceValue.text = decrypt(payoutHistoryModel.data?.total_coins.toString())
                    } else mBinding.txtBalanceValue.text = "NA"

                    if (payoutHistoryModel.data != null ) mBinding.rvPayoutHistory.adapter = HistoryAdapter(activity, payoutHistoryModel.data.payouts)
                    else showErrorScreen()
                }
                DefaultKeyHelper.failureCode -> {
                    showToast(context, decrypt(payoutHistoryModel.message))
                    showErrorScreen()
                }
                DefaultKeyHelper.forceLogoutCode -> {
                    DefaultHelper.forceLogout(activity)
                }
            } else showErrorScreen()
        })
    }

    private fun showErrorScreen() {
        mBinding.clData.visibility = GONE
        mBinding.llError.visibility = VISIBLE
    }

}