package com.app.just_money.my_wallet.faq

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
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentFaqBinding
import com.app.just_money.my_wallet.faq.adapter.FaqAdapter
import com.app.just_money.my_wallet.faq.model.FaqData
import javax.inject.Inject

class FaqFragment : Fragment() {
    @Inject
    lateinit var api: API

    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var faqAdapter: FaqAdapter
    private lateinit var viewModel: FaqViewModel
    private lateinit var mBinding: FragmentFaqBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_faq, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(false)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(FaqViewModel::class.java)
        getFaq()
        mBinding.txtFaqTitle.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun getFaq() {
        mBinding.shimmer.startShimmer()
        viewModel.getFaq(context!!, api).observe(viewLifecycleOwner, { faqDetails ->
            mBinding.shimmer.stopShimmer()
            mBinding.shimmer.visibility = GONE
            mBinding.nsv.visibility = VISIBLE
            if (faqDetails != null) {
                when (faqDetails.status) {
                    DefaultKeyHelper.successCode -> {
                        println("faqDetails: " + faqDetails.faqData?.size)
                        setAdapter(faqDetails.faqData)
                    }
                    DefaultKeyHelper.failureCode -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(faqDetails.message.toString()))
                        activity?.onBackPressed()
                    }
                    DefaultKeyHelper.forceLogoutCode -> {
                        DefaultHelper.forceLogout(activity!!)
                    }
                    else -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(faqDetails.message.toString()))
                        activity?.onBackPressed()
                    }
                }
            } else {
                DefaultHelper.showToast(context, "Something went Wrong!!")
                activity?.onBackPressed()
            }
        })
    }

    private fun setAdapter(faqData: List<FaqData>?) {
        if (faqData?.isNotEmpty() == true) {
            faqAdapter = FaqAdapter(activity!!, faqData)
            mBinding.rvFaq.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            mBinding.rvFaq.adapter = faqAdapter
        } else {
            DefaultHelper.showToast(context, "Something went Wrong!!")
            activity?.onBackPressed()
        }
    }

}