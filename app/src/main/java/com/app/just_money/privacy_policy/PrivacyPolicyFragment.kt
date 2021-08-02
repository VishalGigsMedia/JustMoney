package com.app.just_money.privacy_policy

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultHelper.decrypt
import com.app.just_money.common_helper.DefaultHelper.showToast
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentTermsConditionBinding
import com.app.just_money.terms_condition.TCModel
import kotlinx.android.synthetic.main.fragment_settings_new.*
import javax.inject.Inject

class PrivacyPolicyFragment : Fragment() {

    @Inject
    lateinit var api: API
    private lateinit var viewModel: TCModel
    private lateinit var mBinding: FragmentTermsConditionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_terms_condition, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(TCModel::class.java)
        mBinding.tvTitle.text= getString(R.string.privacy_policy)

        manageClicks()

        mBinding.shimmer.startShimmer()
        Handler(Looper.getMainLooper()).postDelayed({
            getPrivacyPolicy()
        },1000)

    }

    private fun manageClicks() {
        mBinding.tvTitle.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    private fun getPrivacyPolicy() {
        viewModel.getPP(context!!, api).observe(viewLifecycleOwner, { model ->
            mBinding.shimmer.stopShimmer()
            mBinding.shimmer.visibility = View.GONE
            mBinding.nsv.visibility = View.VISIBLE
            if (model != null) {
                when (model.status) {
                    DefaultKeyHelper.successCode -> {
                        mBinding.tvData.text = decrypt(model.data.content)
                    }
                    DefaultKeyHelper.failureCode -> {
                        showToast(context, decrypt(model.message))
                        activity?.onBackPressed()
                    }
                    DefaultKeyHelper.forceLogoutCode -> {
                        DefaultHelper.forceLogout(activity!!)
                    }
                    else -> {
                        showToast(context, decrypt(model.message))
                        activity?.onBackPressed()
                    }
                }
            } else {
                showToast(context, "Something went Wrong!!")
                activity?.onBackPressed()
            }
        })
    }
}