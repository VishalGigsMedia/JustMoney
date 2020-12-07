package com.app.just_money.my_wallet.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentSettingsNewBinding
import com.app.just_money.my_wallet.faq.FaqFragment
import com.app.just_money.my_wallet.setting.view_model.SettingViewModel
import com.app.just_money.privacy_policy.PrivacyPolicyFragment
import com.app.just_money.terms_condition.TermsConditionFragment
import javax.inject.Inject

class SettingsNewFragment : Fragment() {
    @Inject
    lateinit var api: API

    private lateinit var viewModel: SettingViewModel
    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var mBinding: FragmentSettingsNewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_settings_new, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)

        callback?.onShowHideBottomNav(false)
        manageClickEvents()
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun manageClickEvents() {
        mBinding.txtMyProfile.setOnClickListener { openFragment(MyProfileFragment(), true) }
        mBinding.txtFaq.setOnClickListener { openFragment(FaqFragment(), true) }
        mBinding.txtFeedback.setOnClickListener { openFragment(HelpUsFragment(), true) }
        mBinding.txtTermsCondition.setOnClickListener {
            openFragment(
                TermsConditionFragment(),
                true
            )
        }
        mBinding.txtPrivacyPolicy.setOnClickListener {
            openFragment(
                PrivacyPolicyFragment(),
                true
            )
        }
        mBinding.txtFacebook.setOnClickListener { }
        mBinding.txtTwitter.setOnClickListener { }
        mBinding.txtLogout.setOnClickListener {
            logout()
        }
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            //activity!!.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, fragment)
                .addToBackStack(MainActivity::class.java.simpleName)
                .commit()
        } else {
            //supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, fragment)
                .commit()
        }
    }

    private fun logout() {
        viewModel.logout(context!!, api).observe(viewLifecycleOwner, { logoutModule ->
            if (logoutModule != null) {
                when (logoutModule.status) {
                    DefaultKeyHelper.successCode -> {
                        val preferenceHelper = PreferenceHelper(context!!)
                        preferenceHelper.setUserLoggedIn(false)
                        DefaultHelper.forceLogout(activity!!)
                    }
                    DefaultKeyHelper.failureCode -> {
                        DefaultHelper.showToast(
                            context!!,
                            DefaultHelper.decrypt(logoutModule.message.toString())
                        )
                    }
                }
            }
        })
    }
}