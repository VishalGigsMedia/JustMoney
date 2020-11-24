package com.app.justmoney.my_wallet

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.app.justmoney.MainActivity
import com.app.justmoney.R
import com.app.justmoney.databinding.FragmentMyWalletBinding
import com.app.justmoney.my_wallet.completed.CompletedFragment
import com.app.justmoney.my_wallet.payouts.MyPayoutFragment
import com.app.justmoney.my_wallet.setting.SettingFragment

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

        manageClickEvents()

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
        openFragment(SettingFragment(), true)
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

    private fun showCollectAmountDialog() {
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_request_payout)
        dialog.window?.setGravity(Gravity.CENTER)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.setLayout(width, height)
        val clOkay = dialog.findViewById<ConstraintLayout>(R.id.clOkay)
        clOkay.setOnClickListener {
            vibrateDevice()
            dialog.dismiss()
        }
        dialog.show()

    }

    private fun vibrateDevice() {
        val v = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v!!.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            v!!.vibrate(500)
        }
    }

}