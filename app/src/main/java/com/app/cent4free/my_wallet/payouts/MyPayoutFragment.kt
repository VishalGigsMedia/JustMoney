package com.app.cent4free.my_wallet.payouts

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
import com.app.cent4free.MainActivity
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.common_helper.DefaultHelper.forceLogout
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.common_helper.DefaultKeyHelper.failureCode
import com.app.cent4free.common_helper.DefaultKeyHelper.forceLogoutCode
import com.app.cent4free.common_helper.DefaultKeyHelper.successCode
import com.app.cent4free.common_helper.OnCurrentFragmentVisibleListener
import com.app.cent4free.common_helper.PaginationScrollListener
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.MyApplication
import com.app.cent4free.databinding.FragmentMyPayoutBinding
import com.app.cent4free.my_wallet.payouts.adapter.HistoryAdapter
import com.app.cent4free.my_wallet.payouts.model.Payout
import com.app.cent4free.my_wallet.payouts.view_model.MyPayoutViewModel
import javax.inject.Inject

class MyPayoutFragment : Fragment() {
    @Inject
    lateinit var api: API
    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var viewModel: MyPayoutViewModel
    private lateinit var mBinding: FragmentMyPayoutBinding

    private var offset = 0
    private var nextLimit = 10
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var list: ArrayList<Payout> = ArrayList()
    private var adapter: HistoryAdapter? = null
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_payout, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        manageClicks()
        setAdapter()
        addScrollListener()
        getPayoutHistory(offset, nextLimit)

        mBinding.swipe.setOnRefreshListener {
            offset = 0
            nextLimit = 10
            adapter?.clear()
            getPayoutHistory(offset, nextLimit)
        }
    }

    private fun manageClicks() {
        mBinding.txtTitle.setOnClickListener {
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

    private fun getPayoutHistory(offset: Int, nextLimit: Int) {
        if (mBinding.swipe.isRefreshing) {
            mBinding.swipe.isRefreshing = false
        }
        showProgress()
        viewModel.getPayoutHistory(context, api, offset, nextLimit)
            .observe(viewLifecycleOwner, { payoutHistoryModel ->
                hideProgress()
                if (payoutHistoryModel != null) when (payoutHistoryModel.status) {
                    successCode -> {
                        showDataScreen()
                        if (decrypt(payoutHistoryModel.data?.total_coins.toString()).isNotEmpty()) {
                            mBinding.txtBalanceValue.text =
                                decrypt(payoutHistoryModel.data?.total_coins.toString())
                        } else mBinding.txtBalanceValue.text = "NA"

                        if (payoutHistoryModel.data?.payouts?.isNotEmpty()!!) {
                            this.offset = 10
                            this.list = payoutHistoryModel.data.payouts as ArrayList<Payout>
                            adapter?.addData(list)
                        } else showErrorScreen()
                    }
                    failureCode -> {
                        showToast(context, decrypt(payoutHistoryModel.message))
                        showErrorScreen()
                    }
                    forceLogoutCode -> {
                        forceLogout(activity)
                    }
                } else showErrorScreen()
            })
    }

    private fun getMoreData(offset: Int, nextLimit: Int) {
        //showProgress()
        viewModel.getPayoutHistory(activity, api, offset, nextLimit)
            .observe(viewLifecycleOwner, { payoutHistoryModel ->
                //hideProgress()
                if (payoutHistoryModel != null) {
                    try {
                        when (payoutHistoryModel.status) {
                            successCode -> {
                                if (payoutHistoryModel.data?.payouts?.isNotEmpty()!!) {
                                    isLoading = false
                                    this.offset += 10
                                    adapter?.addData(payoutHistoryModel.data.payouts as ArrayList<Payout>)
                                }
                            }
                            failureCode -> {
                            }
                            forceLogoutCode -> forceLogout(activity)
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            })
    }


    private fun hideProgress() {
        mBinding.shimmer.stopShimmer()
        mBinding.shimmer.visibility = GONE
        mBinding.nsv.visibility = VISIBLE
    }

    private fun showProgress() {
        mBinding.shimmer.startShimmer()
        mBinding.shimmer.visibility = VISIBLE
        mBinding.nsv.visibility = GONE
    }

    private fun setAdapter() {
        layoutManager = LinearLayoutManager(context)
        mBinding.rvPayoutHistory.layoutManager = layoutManager
        adapter = HistoryAdapter(activity, list)
        mBinding.rvPayoutHistory.adapter = adapter
        adapter?.notifyDataSetChanged()
    }

    private fun addScrollListener() {
        mBinding.rvPayoutHistory.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                //you have to call load more items to get more data
                getMoreData(offset, nextLimit)
            }
        })
    }

    private fun showErrorScreen() {
        mBinding.clData.visibility = GONE
        mBinding.llError.visibility = VISIBLE
    }
    private fun showDataScreen() {
        mBinding.clData.visibility = VISIBLE
        mBinding.llError.visibility = GONE
    }
}