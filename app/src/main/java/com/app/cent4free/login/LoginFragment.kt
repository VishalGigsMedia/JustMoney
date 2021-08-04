package com.app.cent4free.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.app.cent4free.MainActivity
import com.app.cent4free.R
import com.app.cent4free.available.AvailableOfferViewModel
import com.app.cent4free.available.model.IpApiModel
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.common_helper.DefaultHelper.getCarrierName
import com.app.cent4free.common_helper.DefaultHelper.openFragment
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.common_helper.DefaultKeyHelper
import com.app.cent4free.common_helper.DefaultKeyHelper.APP_LOGIN
import com.app.cent4free.common_helper.DefaultKeyHelper.GOOGLE_LOGIN
import com.app.cent4free.common_helper.DefaultKeyHelper.failureCode
import com.app.cent4free.common_helper.DefaultKeyHelper.successCode
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.MyApplication
import com.app.cent4free.databinding.FragmentLoginBinding
import com.app.cent4free.login.model.login.LoginModel
import com.app.cent4free.login.view_model.LoginViewModel
import com.app.cent4free.terms_condition.TermsConditionFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetDialog
import javax.inject.Inject


class LoginFragment : Fragment() {
    @Inject
    lateinit var api: API

    private var fm: FragmentManager? = null
    private lateinit var mBinding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var viewModelAO: AvailableOfferViewModel
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    val RC_SIGN_IN = 100

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        manageClickEvent()
        getIPAddress()
    }

    private fun init() {
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModelAO = ViewModelProvider(this).get(AvailableOfferViewModel::class.java)
        fm = activity?.supportFragmentManager

        val darkStateList = ContextCompat.getColorStateList(context!!, R.color.checkbox_filter_tint)
        CompoundButtonCompat.setButtonTintList(mBinding.checkbox, darkStateList)

        //google
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            if (account.email != null && account.id != null) {
                onLogin(GOOGLE_LOGIN, account.email, account.id)
            } else showToast(context, getString(R.string.error_social_login))
        } catch (e: ApiException) {
            Log.w("google", "signInResult:failed code=" + e.statusCode)
            showToast(context, getString(R.string.error_social_login))
        }
    }

    private fun manageClickEvent() {
        mBinding.clRegisterWithUs.setOnClickListener {
            openFragment(this.fm, RegisterFragment(), true)
        }
        mBinding.clTermsCondition.setOnClickListener {
            openFragment(this.fm, TermsConditionFragment(), true)
        }
        mBinding.clLogin.setOnClickListener {
            isValid()
        }

        mBinding.txtForgetPassword.setOnClickListener {
            isValidEmailId()
        }
        mBinding.ivGoogle.setOnClickListener {
            val signInIntent: Intent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }

    }

    private fun isValidEmailId() {
        val email = mBinding.edtEmailId.text.toString()
        if (!viewModel.isValidEmail(context, email)) {
            return
        }
        showForgotPasswordDialog(email)
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

        onLogin(APP_LOGIN, email, password)
    }

    private fun getIPAddress() {
        viewModelAO.getIPAddress(context, api).observe(viewLifecycleOwner, fun(ipAddressModel: IpApiModel?) {
            if (ipAddressModel != null) {
                if (ipAddressModel.query !== null && ipAddressModel.query != "") {
                    val preferenceHelper = PreferenceHelper(context)
                    preferenceHelper.setIpAddress(ipAddressModel.query)
                } else {
                    showToast(context, getString(R.string.somethingWentWrong))
                    activity?.finish()
                }
            }
        })
    }

    private fun onLogin(login_type: String, email: String, password_or_id: String) {
        showLoader()
        viewModel.login(activity!!, api, login_type, email, password_or_id, getCarrierName(context))
            .observe(viewLifecycleOwner, { loginModel ->
                hideLoader()
                if (loginModel != null) {
                    when (loginModel.status) {
                        successCode -> {
                            showToast(context, decrypt(loginModel.message.toString()))
                            setLoginData(loginModel)
                        }
                        failureCode -> {
                            showToast(context, decrypt(loginModel.message.toString()))
                        }
                        else -> {
                            showToast(context, decrypt(loginModel.message.toString()))
                        }
                    }
                } else showToast(context, getString(R.string.somethingWentWrong))
            })
    }

    private fun forgotPassword(emailId: String, progressBar: ProgressBar, txtOkay: TextView,
        dialog: BottomSheetDialog) {
        dialog.setCancelable(false)
        txtOkay.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
        //api call
        viewModel.forgotPassword(activity!!, api, emailId).observe(viewLifecycleOwner, { forgotPasswordModel ->
            progressBar.visibility = View.GONE
            txtOkay.visibility = View.VISIBLE
            if (dialog.isShowing) {
                dialog.setCancelable(true)
                dialog.dismiss()
            }
            if (forgotPasswordModel != null) {
                when (forgotPasswordModel.status) {
                    DefaultKeyHelper.successCode -> {
                        showToast(context!!, decrypt(forgotPasswordModel.message))
                    }
                    DefaultKeyHelper.failureCode -> {
                        showToast(context!!, decrypt(forgotPasswordModel.message))
                    }
                    else -> {
                        showToast(context!!, decrypt(forgotPasswordModel.message))
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

        val userId = loginModel.data?.id.toString()
        val userFirstName = loginModel.data?.firstname.toString()
        val userLastName = loginModel.data?.lastname.toString()
        val email = loginModel.data?.email.toString()
        val mobile = loginModel.data?.mobile.toString()
        val dob = loginModel.data?.dob.toString()
        val gender = loginModel.data?.gender.toString()
        val profilePic = loginModel.data?.profilePic.toString()
        val referralCode = loginModel.data?.referral_code.toString()
        //println("profilePic : $profilePic")
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
        if (mobile.isNotEmpty() && mobile != "null") {
            preferenceHelper.setMobile(mobile)
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
        if (referralCode.isNotEmpty() && referralCode != "null") {
            preferenceHelper.setReferralCode(referralCode)
        }
        preferenceHelper.setUserLoggedIn(true)

        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


    private fun showForgotPasswordDialog(email: String) {
        // val dialog = Dialog(context!!)
        // val wrappedContext = ContextThemeWrapper(context, R.style.AppBottomSheetDialogTheme)
        val dialog = BottomSheetDialog(context!!, R.style.AppBottomSheetDialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_forgot_password)

        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        dialog.window!!.attributes = lp

        val txtEmailId = dialog.findViewById<View>(R.id.txtEmailId) as TextView
        val txtOkay = dialog.findViewById<View>(R.id.txtOkay) as TextView
        val progressBar = dialog.findViewById<View>(R.id.progressBar) as ProgressBar

        if (email.isNotEmpty()) {
            txtEmailId.text = email
        }

        txtOkay.setOnClickListener {

            forgotPassword(email, progressBar, txtOkay, dialog)
        }
        dialog.show()
    }

}