package com.app.just_money.my_wallet.payouts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.databinding.FragmentMyPayoutBinding
import com.app.just_money.my_wallet.payouts.adapter.HistoryAdapter
import com.app.just_money.my_wallet.payouts.adapter.PayoutAdapter

class MyPayoutFragment : Fragment() {

    private var callback: OnCurrentFragmentVisibleListener? = null
    private val payoutList: List<String> = ArrayList()
    private lateinit var payoutAdapter: PayoutAdapter
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var mBinding: FragmentMyPayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_payout, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(false)
        setPayoutAdapter()
        mBinding.txtPayouts.setOnClickListener { setPayoutAdapter() }
        mBinding.txtHistory.setOnClickListener { setHistoryAdapter() }
    }
    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }
    private fun setPayoutAdapter() {
        mBinding.viewPayouts.background =
            (ContextCompat.getDrawable(context!!, R.drawable.curve_top_right_bottom_right_pink_600))
        mBinding.viewHistory.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_500))
        mBinding.txtPayouts.setTextColor(ContextCompat.getColor(context!!, R.color.pink_600))
        mBinding.txtHistory.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        payoutAdapter = PayoutAdapter(activity!!, payoutList)
        mBinding.rvPayoutHistory.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvPayoutHistory.adapter = payoutAdapter
        payoutAdapter.notifyDataSetChanged()
    }

    private fun setHistoryAdapter() {
        mBinding.viewHistory.background = (ContextCompat.getDrawable(
            context!!,
            R.drawable.curve_top_left_bottom_right_pink_600
        ))
        mBinding.viewPayouts.setBackgroundColor(ContextCompat.getColor(context!!, R.color.grey_500))
        mBinding.txtHistory.setTextColor(ContextCompat.getColor(context!!, R.color.pink_600))
        mBinding.txtPayouts.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        historyAdapter = HistoryAdapter(activity!!, payoutList)
        mBinding.rvPayoutHistory.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvPayoutHistory.adapter = historyAdapter
        historyAdapter.notifyDataSetChanged()
    }


}