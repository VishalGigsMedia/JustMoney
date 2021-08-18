package com.app.cent4free.my_wallet.change_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.common_helper.DefaultHelper.logout
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.common_helper.DefaultKeyHelper
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.MyApplication
import com.app.cent4free.databinding.FragmentChangePasswordBinding
import com.app.cent4free.my_wallet.setting.view_model.SettingViewModel
import javax.inject.Inject

class ChangePasswordFragment : Fragment() {

    @Inject
    lateinit var api: API
    private lateinit var viewModel: SettingViewModel
    private lateinit var mBinding: FragmentChangePasswordBinding
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        manageClickEvent()
    }

    private fun init() {
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(SettingViewModel::class.java)
        preferenceHelper = PreferenceHelper(context)
    }

    private fun manageClickEvent() {
        mBinding.txtChangePassword.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        mBinding.txtResetPassword.setOnClickListener {
            if (viewModel.isValidPassword(context, mBinding.edtOldPassword.text.toString(),
                    mBinding.edtNewPassword.text.toString(), mBinding.edtConfirmPassword.text.toString())
            ) {
                showLoader()
                viewModel.changePassword(context, api, mBinding.edtOldPassword.text.toString(),
                    mBinding.edtNewPassword.text.toString()).observe(viewLifecycleOwner, { model ->
                    hideLoader()
                    if (model != null) {
                        when (model.status) {
                            DefaultKeyHelper.successCode -> {
                                showToast(context, decrypt(model.message.toString()))
                                logout(viewModel, context, api, viewLifecycleOwner, activity, preferenceHelper)
                            }
                            DefaultKeyHelper.failureCode -> {
                                showToast(context, decrypt(model.message.toString()))
                            }
                            else -> {
                                showToast(context, decrypt(model.message.toString()))
                            }
                        }
                    }
                })
            }
        }
    }

    private fun showLoader() {
        mBinding.progress.visibility = VISIBLE
        mBinding.txtResetPassword.isEnabled = false
    }

    private fun hideLoader() {
        mBinding.progress.visibility = GONE
        mBinding.txtResetPassword.isEnabled = true
    }


}