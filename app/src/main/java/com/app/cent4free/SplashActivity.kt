package com.app.cent4free

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.common_helper.DefaultHelper.getVersionName
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.databinding.ActivitySplashBinding
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    private lateinit var preferenceHelper: PreferenceHelper
    private lateinit var mBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        preferenceHelper = PreferenceHelper(this)

        //version
        mBinding.tvVersion.text = "${getString(R.string.app_name)} -  ${getVersionName()}"

        Timer().schedule(2500) { openMainScreen() }
    }

    private fun openMainScreen() {
        val preferenceHelper = PreferenceHelper(this)

        if (preferenceHelper.isUserLoggedIn()) getIntentData()
        else startActivity(Intent(this, LoginActivity::class.java))

        finish()
    }

    private fun getIntentData() {
        if (intent.hasExtra("notification_type")) {
            val notificationType = intent.getStringExtra("notification_type").toString()
            val offerId = intent.getStringExtra("offer_id").toString()
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("notification_type", decrypt(notificationType))
            intent.putExtra("offer_id", decrypt(offerId))
            startActivity(intent)
        } else startActivity(Intent(this@SplashActivity, MainActivity::class.java))
    }

}