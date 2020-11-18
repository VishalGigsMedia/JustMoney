package com.app.justmoney.my_wallet.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.justmoney.R
import com.app.justmoney.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var mBinding: FragmentSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manageClickEvents()
    }

    private fun manageClickEvents() {
        mBinding.ivClose.setOnClickListener {
            activity!!.supportFragmentManager.popBackStack()
        }
    }


}