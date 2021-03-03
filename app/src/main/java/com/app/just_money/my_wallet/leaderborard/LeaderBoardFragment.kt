package com.app.just_money.my_wallet.leaderborard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.DefaultKeyHelper.weekly
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentLeaderBoardBinding
import com.app.just_money.my_wallet.leaderborard.adapter.LeaderBoardAdapter
import com.app.just_money.my_wallet.leaderborard.model.CurrUserRankMonthly
import com.app.just_money.my_wallet.leaderborard.model.CurrUserRankWeekly
import com.app.just_money.my_wallet.leaderborard.model.Leadership
import javax.inject.Inject

class LeaderBoardFragment : Fragment() {
    @Inject
    lateinit var api: API
    lateinit var mBinding: FragmentLeaderBoardBinding
    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var viewModel: LeaderBoardViewModel
    var currentUserWeekly: CurrUserRankWeekly? = null
    var currentUserMonthly: CurrUserRankMonthly? = null
    var weeklyList: List<Leadership>? = null
    var monthlyList: List<Leadership>? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_leader_board, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(false)
        viewModel = ViewModelProvider(this).get(LeaderBoardViewModel::class.java)
        MyApplication.instance.getNetComponent()?.inject(this)

        getLeaderBoardData(weekly, true)
        manageClicks()
    }

    private fun getLeaderBoardData(type: String, showShimmer: Boolean = false) {
        showShimmer(showShimmer)
        viewModel.getLeaderBoard(context!!, api, type).observe(viewLifecycleOwner, { leaderBoardModel ->
            stopShimmer(showShimmer)
            if (leaderBoardModel != null) {
                when (leaderBoardModel.status) {
                    DefaultKeyHelper.successCode -> {
                        currentUserWeekly = leaderBoardModel.curr_user_rank_weekly
                        currentUserMonthly = leaderBoardModel.curr_user_rank_monthly
                        weeklyList = leaderBoardModel.data.leadership_weekly
                        monthlyList = leaderBoardModel.data.leadership_monthly
                        setWeeklyData()
                    }
                    DefaultKeyHelper.failureCode -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(leaderBoardModel.message))
                        showErrorScreen()
                    }
                    DefaultKeyHelper.forceLogoutCode -> {
                        DefaultHelper.forceLogout(activity!!)
                    }
                    else -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(leaderBoardModel.message))
                        showErrorScreen()
                    }
                }
            } else {
                DefaultHelper.showToast(context, "Something went Wrong!!")
                showErrorScreen()
            }
        })
    }

    private fun setWeeklyData() {
        if (currentUserWeekly != null) {
            mBinding.tvRank.text = DefaultHelper.decrypt(currentUserWeekly?.rank.toString())
            mBinding.tvCoins.text = DefaultHelper.decrypt(currentUserWeekly?.total_amount.toString())
            mBinding.tvName.text = DefaultHelper.decrypt(currentUserWeekly?.firstname.toString())
            DefaultHelper.loadImage(context, DefaultHelper.decrypt(currentUserWeekly?.profile_pic.toString()),
                mBinding.ivProfileImage, ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder),
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder))
            if (weeklyList !== null) mBinding.rvLeaderBoard.adapter = LeaderBoardAdapter(context, weeklyList!!)
            else showErrorScreen()
        } else showErrorScreen()
    }
    private fun setMonthlyData() {
        if (currentUserMonthly != null) {
            mBinding.tvRank.text = DefaultHelper.decrypt(currentUserMonthly?.rank.toString())
            mBinding.tvCoins.text = DefaultHelper.decrypt(currentUserMonthly?.total_amount.toString())
            mBinding.tvName.text = DefaultHelper.decrypt(currentUserMonthly?.firstname.toString())
            DefaultHelper.loadImage(context, DefaultHelper.decrypt(currentUserMonthly?.profile_pic.toString()),
                mBinding.ivProfileImage, ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder),
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder))
            if (monthlyList !== null) mBinding.rvLeaderBoard.adapter = LeaderBoardAdapter(context, monthlyList!!)
            else showErrorScreen()
        } else showErrorScreen()
    }

    private fun showErrorScreen() {
        mBinding.clData.visibility = GONE
        mBinding.llError.visibility = VISIBLE
    }

    private fun stopShimmer(showShimmer: Boolean) {
        if (!showShimmer) return
        mBinding.shimmer.stopShimmer()
        mBinding.shimmer.visibility = GONE
        mBinding.llLeaderBoard.visibility = VISIBLE
    }

    private fun showShimmer(showShimmer: Boolean) {
        if (!showShimmer) return
        mBinding.shimmer.startShimmer()
        mBinding.shimmer.visibility = VISIBLE
        mBinding.llLeaderBoard.visibility = GONE
    }

    private fun manageClicks() {
        mBinding.tvHeading.setOnClickListener {
            activity?.onBackPressed()
        }
        mBinding.tvWeekly.setOnClickListener {
            setWeeklyData()
            mBinding.tvWeekly.background = ContextCompat.getDrawable(context!!, R.drawable.curve_yellow)
            mBinding.tvMonthly.background = null
        }
        mBinding.tvMonthly.setOnClickListener {
            setMonthlyData()
            mBinding.tvWeekly.background = null
            mBinding.tvMonthly.background = ContextCompat.getDrawable(context!!, R.drawable.curve_yellow)
        }
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }
}