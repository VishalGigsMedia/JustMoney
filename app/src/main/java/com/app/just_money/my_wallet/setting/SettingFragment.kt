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
import com.app.just_money.databinding.FragmentSettingBinding
import com.app.just_money.privacy_policy.PrivacyPolicyFragment
import com.app.just_money.terms_condition.TermsConditionFragment

class SettingFragment : Fragment() {
    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var mBinding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
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

    private fun manageClickEvents() {
        mBinding.ivClose.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
        mBinding.txtTermsOfService.setOnClickListener {
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

        mBinding.cLHaveFeedback.setOnClickListener {
            openFragment(HelpUsFragment(), true)
        }
    }


    private fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, fragment)
                .addToBackStack(MainActivity::class.java.simpleName)
                .commit()
        } else {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, fragment)
                .commit()
        }
    }


}