package com.app.just_money.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.DefaultValidationHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentLoginBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import com.poovam.pinedittextfield.PinField
import org.jetbrains.annotations.NotNull
import javax.inject.Inject

class LoginFragment : Fragment() {
    @Inject
    lateinit var api: API

    private val TAG: String = LoginFragment::class.java.simpleName
    private var countryCode: String = "91"

    private lateinit var mBinding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)


        // val linePinField: LinePinField = findViewById(R.id.lineField)
        mBinding.squareField.onTextCompleteListener = object : PinField.OnTextCompleteListener {
            override fun onTextComplete(@NotNull enteredText: String): Boolean {
                //Toast.makeText(this, enteredText, Toast.LENGTH_SHORT).show()
                return true // Return false to keep the keyboard open else return true to close the keyboard
            }
        }


        mBinding.txtVerifyPhoneNumber.setOnClickListener { onClickVerifyPhoneNumber() }
        mBinding.txtVerifyOtp.setOnClickListener {

            onVerify()
        }
        mBinding.txtResend.setOnClickListener { onClickVerifyPhoneNumber() }
    }

    private fun onClickVerifyPhoneNumber() {
        if (!DefaultValidationHelper.isValidMobileNumber(getMobileNumber())) {
            DefaultHelper.showToast(context!!, context!!.getString(R.string.err_ent_phone_number))
        } else {
            receiveOTP()
        }
    }

    private fun onVerify() {
        // println("OTP: " + getOTP())
        if (!DefaultValidationHelper.isValidMobileNumber(getMobileNumber())) {
            DefaultHelper.showToast(context!!, context!!.getString(R.string.err_ent_phone_number))
        } else if (!DefaultValidationHelper.isValidString(getOTP())) {
            DefaultHelper.showToast(context!!, context!!.getString(R.string.err_ent_otp))
        } else {
            //verifyLogin(token)
            getToken()
        }
    }

    private fun getMobileNumber(): String {
        return mBinding.edtMobileNumber.text.toString()
    }

    private fun getOTP(): String {
        return mBinding.squareField.text.toString()
    }


    private fun receiveOTP() {
        mBinding.clLoader.visibility = View.VISIBLE
        viewModel.getOTP(activity!!, api, getMobileNumber(), countryCode)
            .observe(viewLifecycleOwner, { getOtpModel ->
                mBinding.clLoader.visibility = View.GONE
                if (getOtpModel != null) {
                    when (getOtpModel.status) {
                        DefaultKeyHelper.successCode -> {
                            DefaultHelper.showToast(
                                context!!,
                                DefaultHelper.decrypt(getOtpModel.message.toString())
                            )
                            setOTP(getOtpModel.otp.toString())
                        }
                        DefaultKeyHelper.failureCode -> {
                            DefaultHelper.showToast(
                                context!!,
                                DefaultHelper.decrypt(getOtpModel.message.toString())
                            )
                        }
                        else -> {
                            DefaultHelper.showToast(
                                context!!,
                                DefaultHelper.decrypt(getOtpModel.message.toString())
                            )
                        }
                    }
                }
            })
    }


    private fun setOTP(otp: String) {
        mBinding.clVerifyMobileNumber.visibility = View.GONE
        mBinding.clOtpHolder.visibility = View.VISIBLE
        mBinding.squareField.setText(DefaultHelper.decrypt(otp))
    }

    private fun trackLogin() {

        viewModel.trackLogin(
            activity!!,
            getMobileNumber(),
            getMobileNumber()
        ).observe(viewLifecycleOwner, { trackLogin ->
            if (trackLogin != null) {
                if (trackLogin.success == false) {
                    trackSignUp()
                } else {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity!!.finish()
                }
            } else {
                trackSignUp()
            }
        })
    }

    private fun trackSignUp() {
        viewModel.trackSignUp(
            activity!!,
            getMobileNumber(),
            getMobileNumber(),
            getMobileNumber(),
            getMobileNumber(),
            "active"
        ).observe(viewLifecycleOwner, { trackSignUp ->
            if (trackSignUp != null) {
                if (trackSignUp.success) {
                    trackLogin()
                }
            }
        })
    }


    private fun getToken(): String {
        var token = ""
        // Get token
        // [START log_reg_token]
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            //Log.d(TAG, msg)
            Log.d("fcmToken: ", token)

            verifyLogin(token)
            // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        })


        return token
    }


    private fun verifyLogin(token: String) {
        mBinding.clLoader.visibility = View.VISIBLE
        viewModel.login(activity!!, api, getMobileNumber(), getOTP(), countryCode, token)
            .observe(viewLifecycleOwner, { loginModel ->
                mBinding.clLoader.visibility = View.GONE
                if (loginModel != null) {
                    when (loginModel.status) {
                        DefaultKeyHelper.successCode -> {
                            DefaultHelper.showToast(
                                context!!,
                                DefaultHelper.decrypt(loginModel.message.toString())
                            )
                            trackLogin()
                            /* val intent = Intent(context, MainActivity::class.java)
                             startActivity(intent)
                             activity!!.finish()*/
                        }
                        DefaultKeyHelper.failureCode -> {
                            DefaultHelper.showToast(
                                context!!,
                                DefaultHelper.decrypt(loginModel.message.toString())
                            )
                        }
                        else -> {
                            DefaultHelper.showToast(
                                context!!,
                                DefaultHelper.decrypt(loginModel.message.toString())
                            )
                        }
                    }
                }
            })
    }

    private fun subscribeToTopic() {
        // [START subscribe_topics]
        Firebase.messaging.subscribeToTopic("weather")
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                Log.d(TAG, msg)
                //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            }
        // [END subscribe_topics]
    }

}