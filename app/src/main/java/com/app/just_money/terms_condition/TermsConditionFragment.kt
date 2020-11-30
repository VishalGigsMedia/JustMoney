package com.app.just_money.terms_condition

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.just_money.R
import com.app.just_money.databinding.FragmentTermsConditionBinding

class TermsConditionFragment : Fragment() {


    private lateinit var mBinding: FragmentTermsConditionBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_terms_condition, container, false)
        return mBinding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url.toString())
                return true
            }
        }

        mBinding.webView.settings.javaScriptEnabled = true
        mBinding.webView.settings.allowContentAccess = true
        mBinding.webView.settings.javaScriptCanOpenWindowsAutomatically = true

        val termsConditionUrl = getString(R.string.terms_condition_url)
        mBinding.webView.loadUrl(termsConditionUrl)
    }


}