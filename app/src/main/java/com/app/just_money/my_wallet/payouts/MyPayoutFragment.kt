package com.app.just_money.my_wallet.payouts

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
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentMyPayoutBinding
import com.app.just_money.my_wallet.payouts.adapter.HistoryAdapter
import com.app.just_money.my_wallet.payouts.model.PayoutHistoryModel
import com.app.just_money.my_wallet.payouts.view_model.MyPayoutViewModel
import javax.inject.Inject

class MyPayoutFragment : Fragment() {
    @Inject
    lateinit var api: API

    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var viewModel: MyPayoutViewModel
    private lateinit var mBinding: FragmentMyPayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_payout, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getPayoutHistory()
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
        viewModel.getPayoutHistory(context!!, api).observe(viewLifecycleOwner, { payoutHistoryModel ->
            if (payoutHistoryModel != null) {
                when (payoutHistoryModel.status) {
                    DefaultKeyHelper.successCode -> {
                        if (DefaultHelper.decrypt(payoutHistoryModel.points).isNotEmpty()) {
                            mBinding.txtBalanceValue.text = DefaultHelper.decrypt(payoutHistoryModel.points)
                        } else {
                            mBinding.txtBalanceValue.text = "NA"
                        }
                        setHistoryAdapter(payoutHistoryModel.data)
                    }
                    DefaultKeyHelper.failureCode -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(payoutHistoryModel.message))
                    }
                    DefaultKeyHelper.forceLogoutCode -> {
                        DefaultHelper.forceLogout(activity)
                    }
                }
            }
        })
    }

    private fun setHistoryAdapter(data: List<PayoutHistoryModel.Data>) {
        if (data.isNotEmpty()) {
            historyAdapter = HistoryAdapter(activity!!, data)
            mBinding.rvPayoutHistory.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            mBinding.rvPayoutHistory.adapter = historyAdapter
            historyAdapter.notifyDataSetChanged()
        }
    }
}