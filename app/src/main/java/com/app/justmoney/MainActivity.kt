package com.app.justmoney

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.justmoney.available.AvailableFragment
import com.app.justmoney.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //open available fragment
        openFragment(AvailableFragment(), false)
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