package com.app.just_money.my_wallet.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.databinding.FragmentSettingsNewBinding
import com.app.just_money.my_wallet.faq.FaqFragment
import com.app.just_money.privacy_policy.PrivacyPolicyFragment
import com.app.just_money.terms_condition.TermsConditionFragment

class SettingsNewFragment : Fragment() {

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
        callback?.onShowHideBottomNav(false)
        manageClickEvents()
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    fun manageClickEvents() {
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
        mBinding.txtLogout.setOnClickListener { }
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
}