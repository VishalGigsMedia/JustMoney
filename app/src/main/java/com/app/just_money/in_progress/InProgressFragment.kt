package com.app.just_money.in_progress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.just_money.R
import com.app.just_money.available.adapter.QuickDealsAdapter
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentInProgressBinding
import com.app.just_money.in_progress.adapter.InProgressAdapter
import com.app.just_money.my_wallet.completed.model.CompletedOfferData
import javax.inject.Inject

class InProgressFragment : Fragment() {
    @Inject
    lateinit var api: API

    private val blogList: List<String> = ArrayList()
    private lateinit var quickDealsAdapter: QuickDealsAdapter
    private lateinit var inProgressAdapter: InProgressAdapter
    private lateinit var viewModel: InProgressViewModel
    private lateinit var mBinding: FragmentInProgressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(InProgressViewModel::class.java)
        getInProgressOffers()
    }


    private fun getInProgressOffers() {
        viewModel.getInProgressOffers(context!!, api)
            .observe(viewLifecycleOwner, { inProgressOffers ->
                if (inProgressOffers != null) {
                    when (inProgressOffers.status) {
                        DefaultKeyHelper.successCode -> {
                            if (inProgressOffers.completedOfferData != null) {
                                setAdapter(inProgressOffers.completedOfferData)
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

    private fun setAdapter(inProgressOfferData: List<CompletedOfferData>) {

        inProgressAdapter = InProgressAdapter(activity!!, inProgressOfferData)
        mBinding.rvInProgressDeals.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mBinding.rvInProgressDeals.adapter = inProgressAdapter
        inProgressAdapter.notifyDataSetChanged()
    }


}