package com.app.just_money.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentRegisterBinding
import com.app.just_money.login.view_model.RegisterViewModel
import javax.inject.Inject

class RegisterFragment : Fragment() {
    @Inject
    lateinit var api: API

    private var mContext: Context? = null
    private lateinit var viewModel: RegisterViewModel
    private lateinit var mBinding: FragmentRegisterBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        manageClickEvent()
    }

    private fun init() {
        this.mContext = activity
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        MyApplication.instance.getNetComponent()?.inject(this)
    }

    private fun manageClickEvent() {
        mBinding.clRegister.setOnClickListener {
            isValid()
        }
    }

    private fun isValid() {
        val firstName = mBinding.edtFirstName.text.toString()
        val lastName = mBinding.edtLastName.text.toString()
        val emailId = mBinding.edtEmailId.text.toString()
        val password = mBinding.edtPassword.text.toString()
        val confirmPassword = mBinding.edtConfirmPassword.text.toString()

        if (!viewModel.isValidFirstName(context, firstName)) {
            return
        }

        if (!viewModel.isValidLastName(context, lastName)) {
            return
        }

        if (!viewModel.isValidEmail(context, emailId)) {
            return
        }

        if (!viewModel.isValidPassword(context, password, confirmPassword)) {
            return
        }

        //DefaultHelper.showToast(context, "Validation Successful")
        onRegister(firstName, lastName, emailId, password)
    }

    private fun onRegister(firstName: String, lastName: String, emailId: String, password: String) {
        showLoader()
        viewModel.register(mContext, api, firstName, lastName, emailId, password)
            .observe(viewLifecycleOwner, { registerUserModel ->
                hideLoader()
                if (registerUserModel != null) {
                    when (registerUserModel.status) {
                        DefaultKeyHelper.successCode -> {
                            activity?.supportFragmentManager?.popBackStack()
                            DefaultHelper.showToast(mContext, DefaultHelper.decrypt(registerUserModel.message.toString()))
                        }
                        DefaultKeyHelper.failureCode -> {
                            DefaultHelper.showToast(mContext, DefaultHelper.decrypt(registerUserModel.message.toString()))
                        }
                        else -> {
                            DefaultHelper.showToast(mContext, DefaultHelper.decrypt(registerUserModel.message.toString()))
                        }
                    }
                }
            })
    }

    private fun showLoader() {
        mBinding.progressCircular.visibility = View.VISIBLE
        mBinding.clRegister.isEnabled = false
    }

    private fun hideLoader() {
        mBinding.progressCircular.visibility = View.GONE
        mBinding.clRegister.isEnabled = true
    }


}