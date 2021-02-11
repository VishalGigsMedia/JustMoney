package com.app.just_money.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentLoginBinding
import com.app.just_money.login.model.login.LoginModel
import com.app.just_money.login.view_model.LoginViewModel
import com.app.just_money.terms_condition.TermsConditionFragment
import com.google.android.material.textfield.TextInputLayout
import javax.inject.Inject


class LoginFragment : Fragment() {
    @Inject
    lateinit var api: API

    private var fm: FragmentManager? = null
    private lateinit var mBinding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        manageClickEvent()
    }

    private fun init() {
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        fm = activity?.supportFragmentManager
        /* mBinding.edtEmailId.setText("test897@gmail.com")
         mBinding.edtPassword.setText("123456")*/
    }

    private fun TextInputLayout.markRequired() {
        hint = "$hint " + context?.getString(R.string.mandatory_mark)
    }

    private fun manageClickEvent() {
        mBinding.clRegisterWithUs.setOnClickListener {
            DefaultHelper.openFragment(this.fm, RegisterFragment(), true)
        }
        mBinding.clTermsCondition.setOnClickListener {
            DefaultHelper.openFragment(this.fm, TermsConditionFragment(), true)
        }
        mBinding.clLogin.setOnClickListener {
            isValid()
        }

        val darkStateList = ContextCompat.getColorStateList(context!!, R.color.checkbox_filter_tint)
        CompoundButtonCompat.setButtonTintList(mBinding.checkbox, darkStateList)

    }

    private fun isValid() {
        val email = mBinding.edtEmailId.text.toString()
        val password = mBinding.edtPassword.text.toString()
        val isChecked = mBinding.checkbox.isChecked

        if (!viewModel.isValidEmail(context, email)) {
            return
        }
        if (!viewModel.isValidPassword(context, password)) {
            return
        }
        if (!viewModel.isValidTnc(context, isChecked)) {
            return
        }

        onLogin(email, password)
    }


    private fun onLogin(email: String, password: String) {
        showLoader()
        viewModel.login(activity!!, api, email, password, DefaultHelper.getCarrierName(context))
            .observe(viewLifecycleOwner, { loginModel ->
                hideLoader()
                if (loginModel != null) {
                    when (loginModel.status) {
                        DefaultKeyHelper.successCode -> {
                            DefaultHelper.showToast(context!!, DefaultHelper.decrypt(loginModel.message.toString()))
                            setLoginData(loginModel)
                        }
                        DefaultKeyHelper.failureCode -> {
                            DefaultHelper.showToast(context!!, DefaultHelper.decrypt(loginModel.message.toString()))
                        }
                        else -> {
                            DefaultHelper.showToast(context!!, DefaultHelper.decrypt(loginModel.message.toString()))
                        }
                    }
                }
            })
    }

    private fun showLoader() {
        mBinding.progressCircular.visibility = View.VISIBLE
        mBinding.clLogin.isEnabled = false
    }

    private fun hideLoader() {
        mBinding.progressCircular.visibility = View.GONE
        mBinding.clLogin.isEnabled = true
    }

    private fun setLoginData(loginModel: LoginModel) {

        val userId = loginModel.data?.userId.toString()
        val userFirstName = loginModel.data?.firstname.toString()
        val userLastName = loginModel.data?.lastname.toString()
        val email = loginModel.data?.email.toString()
        val dob = loginModel.data?.dob.toString()
        val gender = loginModel.data?.gender.toString()
        val profilePic = loginModel.data?.profilePic.toString()
        val preferenceHelper = PreferenceHelper(context)
        if (userId.isNotEmpty() && userId != "null") {
            preferenceHelper.setUserId(userId)
        }
        if (userFirstName.isNotEmpty() && userFirstName != "null") {
            preferenceHelper.setFirstName(userFirstName)
        }
        if (userLastName.isNotEmpty() && userLastName != "null") {
            preferenceHelper.setLastName(userLastName)
        }

        if (email.isNotEmpty() && email != "null") {
            preferenceHelper.setEmail(email)
        }

        if (dob.isNotEmpty() && dob != "null") {
            preferenceHelper.setDob(dob)
        }
        if (gender.isNotEmpty() && gender != "null") {
            preferenceHelper.setGender(gender)
        }

        if (profilePic.isNotEmpty() && profilePic != "null") {
            preferenceHelper.setProfilePic(profilePic)
        }
        preferenceHelper.setUserLoggedIn(true)

        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity!!.finish()
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            //activity?.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flLogin, fragment)
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        } else {
            //activity?.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flLogin, fragment)?.commit()
        }
    }


}