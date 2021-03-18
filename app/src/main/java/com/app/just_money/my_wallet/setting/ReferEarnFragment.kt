package com.app.just_money.my_wallet.setting

import android.R.attr.label
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper.FACEBOOK
import com.app.just_money.common_helper.DefaultKeyHelper.GMAIL
import com.app.just_money.common_helper.DefaultKeyHelper.TWITTER
import com.app.just_money.common_helper.DefaultKeyHelper.WHATSAPP
import com.app.just_money.common_helper.DefaultKeyHelper.playStoreLink
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.common_helper.TrackingEvents.trackReferred
import com.app.just_money.databinding.FragmentReferEarnBinding
import kotlinx.android.synthetic.main.fragment_refer_earn.*


class ReferEarnFragment : Fragment() {
    private lateinit var mBinding: FragmentReferEarnBinding
    private var referText = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_refer_earn, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        manageClicks()
    }

    private fun setData() {
        val preferenceHelper = PreferenceHelper(context)
        if (preferenceHelper.getReferralCode() != "") {
            mBinding.tvReferral.text = preferenceHelper.getReferralCode()
            referText = "${getString(R.string.referral1)}  ${preferenceHelper.getReferralCode()}  ${getString(R.string.referral2)} \n\n$playStoreLink"
        } else {
            DefaultHelper.showToast(context, getString(R.string.referralError))
            activity?.onBackPressed()
        }
    }

    private fun manageClicks() {
        mBinding.tvReferral.setOnClickListener {
            val clipboard: ClipboardManager? = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText(label.toString(), tvReferral.text)
            clipboard?.setPrimaryClip(clip)
            DefaultHelper.showToast(context, getString(R.string.textCopied))
        }
        mBinding.ivFacebook.setOnClickListener {
            DefaultHelper.share(referText, context, FACEBOOK)
            trackReferred(FACEBOOK)
        }
        mBinding.ivTwitter.setOnClickListener {
            DefaultHelper.share(referText, context, TWITTER)
            trackReferred(TWITTER)
        }
        mBinding.ivGMail.setOnClickListener {
            DefaultHelper.share(referText, context, GMAIL)
            trackReferred(GMAIL)
        }
        mBinding.ivWhatsApp.setOnClickListener {
            DefaultHelper.share(referText, context, WHATSAPP)
            trackReferred(WHATSAPP)
        }
        mBinding.ivMore.setOnClickListener {
            DefaultHelper.share(referText, context, "")
            trackReferred("MORE")
        }
        mBinding.tvHeading.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}