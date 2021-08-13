package com.app.cent4free.my_wallet

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.cent4free.MainActivity
import com.app.cent4free.R
import com.app.cent4free.available.AvailableOfferViewModel
import com.app.cent4free.common_helper.*
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.common_helper.DefaultHelper.openFacebookPage
import com.app.cent4free.common_helper.DefaultHelper.showToast
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.MyApplication
import com.app.cent4free.databinding.FragmentMyWalletBinding
import com.app.cent4free.my_wallet.completed.CompletedFragment
import com.app.cent4free.my_wallet.faq.FaqFragment
import com.app.cent4free.my_wallet.leaderborard.LeaderBoardFragment
import com.app.cent4free.my_wallet.payouts.MyPayoutFragment
import com.app.cent4free.my_wallet.setting.SettingsNewFragment
import javax.inject.Inject


class MyWalletFragment : Fragment() {
    @Inject
    lateinit var api: API
    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var mBinding: FragmentMyWalletBinding
    private lateinit var viewModel: AvailableOfferViewModel
    private lateinit var preferenceHelper: PreferenceHelper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_wallet, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(true)

        init()
        getEarnings()
        manageClickEvents()

        mBinding.swipe.setOnRefreshListener { getEarnings() }
    }

    private fun init() {
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(AvailableOfferViewModel::class.java)
        preferenceHelper = PreferenceHelper(context)
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun getEarnings() {
        if (mBinding.swipe.isRefreshing) {
            mBinding.swipe.isRefreshing = false
        }

        mBinding.txtRequestPayout.isEnabled = false
        val animator = ValueAnimator()
        animator.setObjectValues(2000, 4000, 0)
        animator.addUpdateListener { animation ->
            mBinding.txtCurrentBalanceValue.text = animation.animatedValue.toString()
            mBinding.txtCompletedCoinsValue.text = animation.animatedValue.toString()
            mBinding.txtWithdrawnValue.text = animation.animatedValue.toString()
        }
        animator.duration = 1000 // here you set the duration of the anim
        animator.start()

        viewModel.getEarnings(context, api).observe(viewLifecycleOwner, { model ->
            mBinding.txtRequestPayout.isEnabled = true
            if (model != null) {
                when (model.status) {
                    DefaultKeyHelper.successCode -> {
                        animator.doOnEnd {
                            mBinding.txtCurrentBalanceValue.text = decrypt(model.data.current_balance)
                            mBinding.txtCompletedCoinsValue.text = decrypt(model.data.completed)
                            mBinding.txtWithdrawnValue.text = decrypt(model.data.withdrawn)
                        }
                    }

                    DefaultKeyHelper.failureCode -> {
                        showToast(context, decrypt(model.message))
                        showErrorScreen(animator)
                    }
                }
            } else {
                showToast(context, getString(R.string.somethingWentWrong))
                showErrorScreen(animator)
            }
        })
    }

    private fun showErrorScreen(animator: ValueAnimator) {
        animator.doOnEnd {
            mBinding.txtCurrentBalanceValue.text = "-"
            mBinding.txtCompletedCoinsValue.text = "-"
            mBinding.txtWithdrawnValue.text = "-"
        }
    }

    private fun manageClickEvents() {
        mBinding.ivSetting.setOnClickListener { onClickSetting() }
        mBinding.txtRequestPayout.setOnClickListener { onClickRequestPayout() }
        mBinding.txtPayout.setOnClickListener { onClickPayout() }
        mBinding.txtCompleted.setOnClickListener { onClickCompleted() }
        mBinding.txtQuestion.setOnClickListener { onClickQuestion() }
        mBinding.txtTryAndEnjoy.setOnClickListener { onClickTryEnjoy() }
        mBinding.clFacebook.setOnClickListener { onClickFacebook() }
        mBinding.clTelegram.setOnClickListener { onClickTelegram() }
        mBinding.clLeaderBoard.setOnClickListener { openFragment(LeaderBoardFragment()) }
    }

    private fun onClickSetting() {
        openFragment(SettingsNewFragment())
    }

    private fun onClickRequestPayout() {
        if (preferenceHelper.getMobile() == "") {
            Toast.makeText(context, getString(R.string.complete_profile_error), Toast.LENGTH_SHORT).show();
        } else showCollectAmountDialog()
    }

    private fun onClickPayout() {
        openFragment(MyPayoutFragment())
    }

    private fun onClickCompleted() {
        openFragment(CompletedFragment())
    }

    private fun onClickQuestion() {
        openFragment(FaqFragment())
    }

    private fun onClickTryEnjoy() {}

    private fun onClickFacebook() {
        openFacebookPage(context!!)
        TrackingEvents.trackFBLikeClicked("Wallet")
    }

    private fun onClickTelegram() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(DefaultKeyHelper.telegramUrl)))
        TrackingEvents.trackJoinTelegramClicked("Wallet")
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean = true) {
        if (addToBackStack) {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        } else activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)?.commit()
    }

    private fun showCollectAmountDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.dialog_request_payout)
        dialog?.window?.setGravity(Gravity.CENTER)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        val btnContinue = dialog?.findViewById<TextView>(R.id.btnContinue)
        val tvMobileNumber = dialog?.findViewById<TextView>(R.id.tvMobileNumber)
        val etAmount = dialog?.findViewById<EditText>(R.id.etAmount)
        val pbRequestPayout = dialog?.findViewById<ProgressBar>(R.id.pbRequestPayout)
        val tvClickText = dialog?.findViewById<TextView>(R.id.tvClickText)
        val ivFootLogo = dialog?.findViewById<ImageView>(R.id.ivFootLogo)
        tvMobileNumber?.text = preferenceHelper.getMobile()
        btnContinue?.setOnClickListener {
            if (etAmount?.text.toString() == "") {
                showToast(context, getString(R.string.error_paytm_input2))
                return@setOnClickListener
            }

            if (mBinding.txtCurrentBalanceValue.text.toString() == "-") {
                showToast(context, getString(R.string.somethingWentWrong1))
                dialog.dismiss()
                getEarnings()
                return@setOnClickListener
            } else if (Integer.parseInt(etAmount?.text.toString()) > Integer.parseInt(
                    mBinding.txtCurrentBalanceValue.text.toString())
            ) {
                showToast(context, getString(R.string.error_paytm_input))
                return@setOnClickListener
            }
            //else
            requestPayout(etAmount?.text.toString(), dialog, pbRequestPayout)
        }
        tvClickText?.setOnClickListener {
            openAstroPay()
        }
        ivFootLogo?.setOnClickListener {
            openAstroPay()
        }
        dialog?.show()
    }

    private fun openAstroPay() {
        val browserIntent = Intent(Intent.ACTION_VIEW,
            Uri.parse("https://play.google.com/store/apps/details?id=com.astropaycard.android"))
        startActivity(browserIntent)
    }

    private fun requestPayout(amt: String, dialog: Dialog, pbRequestPayout: ProgressBar?) {
        pbRequestPayout?.visibility = VISIBLE
        viewModel.requestPayout(context, api, amt).observe(viewLifecycleOwner, { model ->
            pbRequestPayout?.visibility = GONE
            if (model != null) {
                when (model.status) {
                    DefaultKeyHelper.successCode -> {
                        showToast(context, getString(R.string.req_success))
                        dialog.dismiss()
                        getEarnings()
                        vibrateDevice()
                    }

                    DefaultKeyHelper.failureCode -> {
                        showToast(context, decrypt(model.message))
                    }
                }
            } else {
                showToast(context, getString(R.string.somethingWentWrong))
            }
        })
    }

    private fun vibrateDevice() {
        val v = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v?.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION") v?.vibrate(200)
        }
    }

}