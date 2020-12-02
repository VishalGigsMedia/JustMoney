package com.app.just_money.my_wallet.completed

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
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentCompletedBinding
import com.app.just_money.my_wallet.completed.adapter.CompletedAdapter
import com.app.just_money.my_wallet.completed.model.CompletedOfferData
import javax.inject.Inject

class CompletedFragment : Fragment() {
    @Inject
    lateinit var api: API

    private var callback: OnCurrentFragmentVisibleListener? = null
    private val blogList: List<String> = ArrayList()
    private lateinit var completedAdapter: CompletedAdapter
    private lateinit var viewModel: CompletedOfferViewModel
    private lateinit var mBinding: FragmentCompletedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_completed, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(false)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(CompletedOfferViewModel::class.java)

        getCompletedOffer()
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun getCompletedOffer() {
        viewModel.getCompletedOffers(context!!, api)
            .observe(viewLifecycleOwner, { completedOffers ->
                if (completedOffers != null) {
                    when (completedOffers.status) {
                        DefaultKeyHelper.successCode -> {
                            if (completedOffers.completedOfferData != null) {
                                setAdapter(completedOffers.completedOfferData)
                            }
                        }
                        DefaultKeyHelper.failureCode -> {

                        }
                        DefaultKeyHelper.forceLogoutCode -> {

                        }
                    }
                }
            })
    }


    private fun setAdapter(completedOfferData: List<CompletedOfferData>) {
        completedAdapter = CompletedAdapter(activity!!, completedOfferData)
        mBinding.rvInProgressDeals.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvInProgressDeals.adapter = completedAdapter
        completedAdapter.notifyDataSetChanged()
    }

}