package com.app.just_money

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.app.just_money.databinding.ActivitySplashBinding
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        Timer().schedule(5000) {
            openMainScreen()
        }

    }

    private fun openMainScreen() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
        /*val sessionManager = SessionManager(this)
        val loggedIn = sessionManager.getBoolean(SessionManager.Key.UserLoggedIn.name)
        println("loggedIn : $loggedIn")
        if (loggedIn) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }*/
    }


}