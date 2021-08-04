package com.app.cent4free

import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.cent4free.available.AvailableFragment
import com.app.cent4free.common_helper.DefaultHelper.playCustomSound
import com.app.cent4free.common_helper.DefaultKeyHelper.availableFragment
import com.app.cent4free.common_helper.DefaultKeyHelper.inProgressFragment
import com.app.cent4free.common_helper.DefaultKeyHelper.walletFragment
import com.app.cent4free.common_helper.OnCurrentFragmentVisibleListener
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.databinding.ActivityMainBinding
import com.app.cent4free.in_progress.InProgressFragment
import com.app.cent4free.my_wallet.MyWalletFragment
import com.app.cent4free.my_wallet.completed.CompletedFragment
import com.app.cent4free.my_wallet.faq.FaqFragment
import com.app.cent4free.my_wallet.leaderborard.LeaderBoardFragment
import com.app.cent4free.my_wallet.payouts.MyPayoutFragment
import com.app.cent4free.my_wallet.setting.SettingsNewFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import java.util.*

class MainActivity : AppCompatActivity(),OnCurrentFragmentVisibleListener {

    private lateinit var mBinding: ActivityMainBinding

    var popup: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        subscribeToTopic()
        getToken()
        manageClickEvents()
        playCustomSound(this, R.raw.load_dashboard)
        //open available fragment
        openFragment(AvailableFragment(), false, availableFragment)
    }


    private fun getToken() {
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("fcm", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.d("fcmToken: ", token)
            val preferenceHelper = PreferenceHelper(this)
            preferenceHelper.setFCMToken(token)
        })
    }

    override fun onBackPressed() {
        val availableFragment: AvailableFragment? =
            supportFragmentManager.findFragmentByTag(availableFragment) as AvailableFragment?
        val inProgressFragment: InProgressFragment? =
            supportFragmentManager.findFragmentByTag(inProgressFragment) as InProgressFragment?
        val walletFragment: MyWalletFragment? =
            supportFragmentManager.findFragmentByTag(walletFragment) as MyWalletFragment?
        if (availableFragment != null && availableFragment.isVisible || inProgressFragment != null && inProgressFragment.isVisible || walletFragment != null && walletFragment.isVisible) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Are you sure you want to Exit?").setCancelable(true)
                .setPositiveButton("Yes") { _, _ ->
                    super.onBackPressed()
                }.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        } else {
            super.onBackPressed()
        }
    }

    private fun subscribeToTopic() {
        // [START subscribe_topics]
        Firebase.messaging.unsubscribeFromTopic("GENERAL").addOnCompleteListener { task ->
            var msg = getString(R.string.msg_subscribed)
            if (!task.isSuccessful) {
                msg = getString(R.string.msg_subscribe_failed)
            }
            Log.d("subscribe", msg)
            //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        // [END subscribe_topics]
    }

    private fun manageClickEvents() {
        mBinding.txtAvailable.setOnClickListener { onClickAvailable() }
        mBinding.txtInProgress.setOnClickListener { onClickInProgress() }
        mBinding.txtMyWallet.setOnClickListener {
            onClickMyWallet()
        }
    }

    private fun onClickAvailable() {
        mBinding.clAvailable.background = ContextCompat.getDrawable(this, R.drawable.curve_blue_grey_900)
        mBinding.clInProgress.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.clMyWallet.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.txtAvailable.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_active_available), null, null, null)
        mBinding.txtMyWallet.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_wallet), null, null, null)
        mBinding.txtInProgress.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_inprogress), null, null, null)
        openFragment(AvailableFragment(), false, availableFragment)
    }

    private fun onClickInProgress() {
        mBinding.clInProgress.background = ContextCompat.getDrawable(this, R.drawable.curve_blue_grey_900)
        mBinding.clAvailable.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.clMyWallet.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.txtAvailable.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_available), null, null, null)
        mBinding.txtMyWallet.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_wallet), null, null, null)
        mBinding.txtInProgress.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_active_inprogress), null, null, null)

        openFragment(InProgressFragment(), false, inProgressFragment)
    }

    private fun onClickMyWallet() {
        mBinding.clMyWallet.background = ContextCompat.getDrawable(this, R.drawable.curve_blue_grey_900)
        mBinding.clAvailable.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.clInProgress.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.txtAvailable.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_available), null, null, null)

        mBinding.txtInProgress.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_inprogress), null, null, null)

        mBinding.txtMyWallet.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_active_wallet), null, null, null)

        openFragment(MyWalletFragment(), false, walletFragment)
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        if (addToBackStack) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction().replace(R.id.flMain, fragment, tag)
                .addToBackStack(MainActivity::class.java.simpleName).commit()
        } else {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction().replace(R.id.flMain, fragment, tag).commit()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is AvailableFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is InProgressFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is CompletedFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is MyWalletFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is FaqFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is MyPayoutFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is SettingsNewFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is LeaderBoardFragment) fragment.setOnCurrentFragmentVisibleListener(this)
    }

    override fun onShowHideBottomNav(show: Boolean) =
        if (show) mBinding.bottomNav.visibility = VISIBLE else mBinding.bottomNav.visibility = GONE


}