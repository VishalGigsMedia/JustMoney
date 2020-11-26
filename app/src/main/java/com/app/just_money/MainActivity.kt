package com.app.just_money

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.just_money.available.AvailableFragment
import com.app.just_money.databinding.ActivityMainBinding
import com.app.just_money.in_progress.InProgressFragment
import com.app.just_money.my_wallet.MyWalletFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        manageClickEvents()

        //open available fragment
        openFragment(AvailableFragment(), false)

    }

    private fun manageClickEvents() {
        mBinding.txtAvailable.setOnClickListener { onClickAvailable() }
        mBinding.txtInProgress.setOnClickListener { onClickInProgress() }
        mBinding.txtMyWallet.setOnClickListener { onClickMyWallet() }
    }

    private fun onClickAvailable() {
        mBinding.clAvailable.background =
            ContextCompat.getDrawable(this, R.drawable.curve_blue_grey_900)
        mBinding.clInProgress.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.clMyWallet.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.txtAvailable.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_active_available
            ), null, null, null
        )
        mBinding.txtMyWallet.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_wallet
            ), null, null, null
        )
        mBinding.txtInProgress.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_inprogress
            ), null, null, null
        )
        openFragment(AvailableFragment(), false)
    }

    private fun onClickInProgress() {
        mBinding.clInProgress.background =
            ContextCompat.getDrawable(this, R.drawable.curve_blue_grey_900)
        mBinding.clAvailable.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.clMyWallet.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.txtAvailable.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_available
            ), null, null, null
        )
        mBinding.txtMyWallet.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_wallet
            ), null, null, null
        )
        mBinding.txtInProgress.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_active_inprogress
            ), null, null, null
        )

        openFragment(InProgressFragment(), false)
    }

    private fun onClickMyWallet() {
        mBinding.clMyWallet.background =
            ContextCompat.getDrawable(this, R.drawable.curve_blue_grey_900)
        mBinding.clAvailable.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.clInProgress.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.txtAvailable.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_available
            ), null, null, null
        )

        mBinding.txtInProgress.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_inactive_inprogress
            ), null, null, null
        )

        mBinding.txtMyWallet.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_active_wallet
            ), null, null, null
        )

        openFragment(MyWalletFragment(), false)
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, fragment)
                .addToBackStack(MainActivity::class.java.simpleName)
                .commit()
        } else {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, fragment)
                .commit()
        }
    }
}