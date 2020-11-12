package com.app.justmoney.my_wallet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.justmoney.MainActivity
import com.app.justmoney.R
import com.app.justmoney.databinding.FragmentMyWalletBinding
import com.app.justmoney.setting.SettingFragment

class MyWalletFragment : Fragment() {

    private lateinit var mBinding: FragmentMyWalletBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_wallet, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun manageClickEvents() {

        mBinding.ivSetting.setOnClickListener { onClickSetting() }
        mBinding.txtRequestPayout.setOnClickListener { onClickRequestPayout() }
        mBinding.txtPayout.setOnClickListener { onClickPayout() }
        mBinding.txtCompleted.setOnClickListener { onClickCompleted() }
        mBinding.txtQuestion.setOnClickListener { onClickQuestion() }
        mBinding.txtTryAndEnjoy.setOnClickListener { onClickTryEnjoy() }
        mBinding.txtFacebook.setOnClickListener { onClickFacebook() }
        mBinding.txtTwitter.setOnClickListener { onClickTwitter() }

    }

    private fun onClickSetting() {
        openFragment(SettingFragment(), false)
    }

    private fun onClickRequestPayout() {}

    private fun onClickPayout() {}

    private fun onClickCompleted() {}

    private fun onClickQuestion() {}

    private fun onClickTryEnjoy() {}

    private fun onClickFacebook() {}

    private fun onClickTwitter() {}

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