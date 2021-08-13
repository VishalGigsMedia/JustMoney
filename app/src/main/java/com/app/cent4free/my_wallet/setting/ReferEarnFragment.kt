package com.app.cent4free.my_wallet.setting

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
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper.share
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.common_helper.DefaultKeyHelper.FACEBOOK
import com.app.cent4free.common_helper.DefaultKeyHelper.GMAIL
import com.app.cent4free.common_helper.DefaultKeyHelper.TWITTER
import com.app.cent4free.common_helper.DefaultKeyHelper.WHATSAPP
import com.app.cent4free.common_helper.DefaultKeyHelper.playStoreLink
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.common_helper.TrackingEvents.trackReferred
import com.app.cent4free.databinding.FragmentReferEarnBinding
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
        mBinding.tvReferral.text = preferenceHelper.getReferralCode()
        referText = "${getString(R.string.referral1)}  ${preferenceHelper.getReferralCode()}  ${
            getString(R.string.referral2)
        } \n\n$playStoreLink"

    }

    private fun manageClicks() {
        mBinding.tvReferral.setOnClickListener {
            val clipboard: ClipboardManager? = context?.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?
            val clip = ClipData.newPlainText(label.toString(), tvReferral.text)
            clipboard?.setPrimaryClip(clip)
            showToast(context, getString(R.string.textCopied))
        }
        mBinding.ivFacebook.setOnClickListener {
            share(referText, activity!!, FACEBOOK)
            trackReferred(FACEBOOK)
        }
        mBinding.ivTwitter.setOnClickListener {
            share(referText, activity!!, TWITTER)
            trackReferred(TWITTER)
        }
        mBinding.ivGMail.setOnClickListener {
            share(referText, activity!!, GMAIL)
            trackReferred(GMAIL)
        }
        mBinding.ivWhatsApp.setOnClickListener {
            share(referText, activity!!, WHATSAPP)
            trackReferred(WHATSAPP)
        }
        mBinding.ivMore.setOnClickListener {
            share(referText, activity!!, "")
            trackReferred("MORE")
        }
        mBinding.tvHeading.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}