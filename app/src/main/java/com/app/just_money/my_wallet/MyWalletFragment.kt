package com.app.just_money.my_wallet

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.databinding.FragmentMyWalletBinding
import com.app.just_money.my_wallet.completed.CompletedFragment
import com.app.just_money.my_wallet.faq.FaqFragment
import com.app.just_money.my_wallet.payouts.MyPayoutFragment
import com.app.just_money.my_wallet.setting.SettingsNewFragment


class MyWalletFragment : Fragment() {

    private var callback: OnCurrentFragmentVisibleListener? = null
    private lateinit var mBinding: FragmentMyWalletBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_wallet, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback?.onShowHideBottomNav(true)
        setData()
        manageClickEvents()
    }

    fun setOnCurrentFragmentVisibleListener(activity: MainActivity) {
        callback = activity
    }

    private fun setData() {
        val preferenceHelper = PreferenceHelper(context)
        val currentBalance = preferenceHelper.getTotalCoins()
        val completedCoins = preferenceHelper.getCompleted()
        val withdrawnCoins = preferenceHelper.getWithdrawn()

        if (currentBalance.isNotEmpty()) mBinding.txtCurrentBalanceValue.text =
            DefaultHelper.decrypt(currentBalance)

        if (completedCoins.isNotEmpty()) mBinding.txtCompletedCoinsValue.text =
            DefaultHelper.decrypt(completedCoins)

        if (withdrawnCoins.isNotEmpty()) mBinding.txtWithdrawnValue.text = DefaultHelper.decrypt(withdrawnCoins)
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
    }

    private fun onClickSetting() {
        openFragment(SettingsNewFragment(), true)
    }

    private fun onClickRequestPayout() {
        showCollectAmountDialog()
    }

    private fun onClickPayout() {
        openFragment(MyPayoutFragment(), true)
    }

    private fun onClickCompleted() {
        openFragment(CompletedFragment(), true)
    }

    private fun onClickQuestion() {
        openFragment(FaqFragment(), true)
    }

    private fun onClickTryEnjoy() {}

    private fun onClickFacebook() {
        DefaultHelper.openFacebookPage(context)
    }

    private fun onClickTelegram() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(DefaultKeyHelper.telegramUrl)))
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        } else activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, fragment)?.commit()
    }

    private fun showCollectAmountDialog() {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(true)
        dialog?.setContentView(R.layout.dialog_request_payout)
        dialog?.window?.setGravity(Gravity.CENTER)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        val clOkay = dialog?.findViewById<ConstraintLayout>(R.id.clOkay)
        clOkay?.setOnClickListener {
            vibrateDevice()
            dialog.dismiss()
        }
        dialog?.show()
    }

    private fun vibrateDevice() {
        val v = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v?.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION") v?.vibrate(500)
        }
    }

}