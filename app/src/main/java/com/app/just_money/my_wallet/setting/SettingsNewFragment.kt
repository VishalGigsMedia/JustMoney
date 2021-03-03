package com.app.just_money.my_wallet.setting

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.R.string.shareAppText
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.DefaultKeyHelper.playStoreLink
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
    /*lateinit var manager: ReviewManager
    var reviewInfo: ReviewInfo? = null*/
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings_new, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        manageClickEvents()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun init() {
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        callback?.onShowHideBottomNav(false)
    }

    private fun manageClickEvents() {
        mBinding.txtMyProfile.setOnClickListener { openFragment(MyProfileFragment()) }
        mBinding.txtFaq.setOnClickListener { openFragment(FaqFragment()) }
        mBinding.txtFeedback.setOnClickListener { openFragment(HelpUsFragment()) }
        mBinding.txtReferNEarn.setOnClickListener { openFragment(ReferEarnFragment()) }
        mBinding.txtShareApp.setOnClickListener {
            val appSharingText= "${getString(shareAppText)} \n\n $playStoreLink"
            DefaultHelper.share(appSharingText,context,"")
        }
        mBinding.txtTermsCondition.setOnClickListener {
            openFragment(TermsConditionFragment())
        }
        mBinding.txtPrivacyPolicy.setOnClickListener {
            openFragment(PrivacyPolicyFragment())
        }
        mBinding.clFacebook.setOnClickListener {
            DefaultHelper.openFacebookPage(context)
        }
        mBinding.clTelegram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(DefaultKeyHelper.telegramUrl)))
        }
        mBinding.txtLogout.setOnClickListener {
            if (context != null) {
                val builder = AlertDialog.Builder(context!!)
                builder.setMessage("Are you sure you want to Logout?").setCancelable(true)
                    .setPositiveButton("Yes") { _, _ ->
                        logout()
                    }.setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }
        mBinding.txtSetting.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun logout() {
        viewModel.logout(context, api).observe(viewLifecycleOwner, { logoutModule ->
            if (logoutModule != null) {
                when (logoutModule.status) {
                    DefaultKeyHelper.successCode -> {
                        val preferenceHelper = PreferenceHelper(context)
                        preferenceHelper.setUserLoggedIn(false)
                        DefaultHelper.forceLogout(activity)
                    }
                    DefaultKeyHelper.failureCode -> {
                        DefaultHelper.showToast(context, DefaultHelper.decrypt(logoutModule.message.toString()))
                    }
                }
            }else DefaultHelper.showToast(context, getString(R.string.somethingWentWrong))
        })
    }

    private fun setData() {
        val preferenceHelper = PreferenceHelper(context)
        mBinding.txtEmail.text = preferenceHelper.getEmail()
        val profilePic = DefaultHelper.decrypt(preferenceHelper.getProfilePic())
        if (profilePic.isNotEmpty() && profilePic != "null") {
            DefaultHelper.loadImage(context, preferenceHelper.getProfilePic(), mBinding.ivProfileImage,
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder),
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder))
        } else {
            mBinding.ivProfileImage.setImageDrawable(
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder))
        }
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        if (addToBackStack) {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        } else {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)?.commit()
        }
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    /*fun reviewPlayStorePopup() {
        manager = ReviewManagerFactory.create(context!!)
        manager.requestReviewFlow().addOnCompleteListener { request ->
            if (request.isSuccessful) {
                reviewInfo = request.result
                if (reviewInfo != null && activity != null) {
                    manager.launchReviewFlow(activity!!, reviewInfo!!).addOnFailureListener {
                        // Log error and continue with the flow
                        Log.d("rateyourapp", "onViewCreated: ")
                    }.addOnCompleteListener {
                        // Log success and continue with the flow
                        Log.d("rateyourapp", "onViewCreated: $it")
                    }
                } else {
                    Log.d("rateyourapp", "nulll:")
                }
            } else {
                Log.d("rateyourapp", "manager nulll:")
            }
        }
    }*/
}