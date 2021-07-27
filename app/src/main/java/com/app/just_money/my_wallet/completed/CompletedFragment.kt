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
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultHelper.decrypt
import com.app.just_money.common_helper.DefaultHelper.forceLogout
import com.app.just_money.common_helper.DefaultHelper.showToast
import com.app.just_money.common_helper.DefaultKeyHelper.failureCode
import com.app.just_money.common_helper.DefaultKeyHelper.forceLogoutCode
import com.app.just_money.common_helper.DefaultKeyHelper.successCode
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.common_helper.PaginationScrollListener
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentCompletedBinding
import com.app.just_money.my_wallet.completed.adapter.CompletedAdapter
import com.app.just_money.my_wallet.completed.model.CompletedList
import com.app.just_money.my_wallet.payouts.model.Payout
import javax.inject.Inject

class CompletedFragment : Fragment() {
    @Inject
    lateinit var api: API
    private var scroll_offset = 0
    private var scroll_limit = 10
    private var isLastPage: Boolean = false
    private var isLoading: Boolean = false
    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var adapter: CompletedAdapter
    private var list: ArrayList<CompletedList> = ArrayList()
    private lateinit var viewModel: CompletedOfferViewModel
    private lateinit var mBinding: FragmentCompletedBinding
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(false)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(CompletedOfferViewModel::class.java)

        setAdapter()
        addScrollListener()
        getCompletedOffer(scroll_offset, scroll_limit)
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

    private fun getCompletedOffer(scroll_offset: Int, scroll_limit: Int) {
        mBinding.shimmer.startShimmer()
        viewModel.getCompletedOffers(context, api, scroll_offset, scroll_limit)
            .observe(viewLifecycleOwner, { completedOffers ->
                mBinding.shimmer.stopShimmer()
                mBinding.shimmer.visibility = GONE
                mBinding.nsv.visibility = VISIBLE
                if (completedOffers != null) {
                    when (completedOffers.status) {
                        successCode -> {
                            if (completedOffers.data?.completedList != null && completedOffers.data.completedList.isNotEmpty()) {
                                mBinding.rvCompleted.visibility = VISIBLE
                                this.scroll_offset = 10
                                this.list = completedOffers.data.completedList as ArrayList<CompletedList>
                                adapter.addData(list)
                            } else showErrorScreen()
                        }
                        failureCode -> {
                            showToast(context, decrypt(completedOffers.message.toString()))
                            showErrorScreen()
                        }
                        forceLogoutCode -> {
                        }
                        else -> {
                            showToast(context, decrypt(completedOffers.message.toString()))
                            showErrorScreen()
                        }
                    }
                } else showErrorScreen()
            })
    }

    private fun setAdapter() {
        if (activity != null) {
            layoutManager = LinearLayoutManager(context)
            mBinding.rvCompleted.layoutManager = layoutManager
            adapter = CompletedAdapter(activity!!, list)
            mBinding.rvCompleted.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun addScrollListener() {
        mBinding.rvCompleted.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

            override fun loadMoreItems() {
                isLoading = true
                //you have to call load more items to get more data
                getMoreData(scroll_offset, scroll_limit)
            }
        })
    }

    private fun getMoreData(scroll_offset: Int, scroll_limit: Int) {
        //showProgress()
        viewModel.getCompletedOffers(activity, api, scroll_offset, scroll_limit).observe(viewLifecycleOwner, { completedOffers ->
            //hideProgress()
            if (completedOffers != null) {
                try {
                    when (completedOffers.status) {
                        successCode -> {
                            if (completedOffers.data?.completedList?.isNotEmpty()!!) {
                                isLoading = false
                                this.scroll_offset += 10
                                adapter.addData(completedOffers.data.completedList as ArrayList<CompletedList>)
                            }
                        }
                        failureCode -> {}
                        forceLogoutCode -> forceLogout(activity)
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun showErrorScreen() {
        mBinding.rvCompleted.visibility = GONE
        mBinding.llError.visibility = VISIBLE
    }

}