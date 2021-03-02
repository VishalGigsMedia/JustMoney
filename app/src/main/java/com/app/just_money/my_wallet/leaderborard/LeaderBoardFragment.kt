package com.app.just_money.my_wallet.leaderborard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.databinding.FragmentLeaderBoardBinding
import com.app.just_money.my_wallet.leaderborard.adapter.LeaderBoardAdapter

class LeaderBoardFragment : Fragment() {
    lateinit var mBinding : FragmentLeaderBoardBinding
    private var callback: OnCurrentFragmentVisibleListener? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_leader_board, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(false)
        getLeaderBoardData()
        manageClicks()
    }

    private fun getLeaderBoardData() {
        mBinding.rvLeaderBoard.adapter=LeaderBoardAdapter(context,null)
    }

    private fun manageClicks() {
        mBinding.tvWeekly.setOnClickListener{
            mBinding.tvWeekly.background = ContextCompat.getDrawable(context!!, R.drawable.curve_pink_600)
            mBinding.tvMonthly.background = null
        }
        mBinding.tvMonthly.setOnClickListener{
            mBinding.tvWeekly.background = null
            mBinding.tvMonthly.background = ContextCompat.getDrawable(context!!, R.drawable.curve_pink_600)
        }
    }
    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }
}