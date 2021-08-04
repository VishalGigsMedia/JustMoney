package com.app.cent4free.my_wallet.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.cent4free.MainActivity
import com.app.cent4free.R
import com.app.cent4free.common_helper.DefaultHelper
import com.app.cent4free.common_helper.DefaultHelper.decrypt
import com.app.cent4free.common_helper.DefaultKeyHelper
import com.app.cent4free.common_helper.PreferenceHelper
import com.app.cent4free.dagger.API
import com.app.cent4free.dagger.MyApplication
import com.app.cent4free.databinding.FragmentProfileBinding
import com.app.cent4free.my_wallet.setting.view_model.ProfileViewModel
import javax.inject.Inject

class MyProfileFragment : Fragment() {
    @Inject
    lateinit var api: API

    private lateinit var viewModel: ProfileViewModel
    private lateinit var mBinding: FragmentProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        //getProfile()
        manageClickEvent()
    }

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        val preferenceHelper = PreferenceHelper(context)
        //Set Name
        mBinding.txtNameValue.text = preferenceHelper.getFirstName() + " " + preferenceHelper.getLastName()

        //Set DOB
        if (preferenceHelper.getDob().contains("0000")) {
            //it means dob not set yet
            mBinding.txtBirthDateValue.text = "N/A"
        } else mBinding.txtBirthDateValue.text = preferenceHelper.getDob()


        //Set mobile
        if (preferenceHelper.getMobile() == "") {
            //it means mobile not set yet
            mBinding.txtMobileValue.text = "N/A"
        } else mBinding.txtMobileValue.text = preferenceHelper.getMobile()

        //Set Gender
        when (preferenceHelper.getGender()) {
            "0" -> mBinding.txtGenderValue.text = "N/A"
            DefaultKeyHelper.male -> mBinding.txtGenderValue.text = "Male"
            DefaultKeyHelper.female -> mBinding.txtGenderValue.text = "Female"
        }

        //Set Email & Image
        mBinding.txtEmailValue.text = preferenceHelper.getEmail()
        val profilePic = decrypt(preferenceHelper.getProfilePic())
        if (profilePic.isNotEmpty() && profilePic != "null") {
            DefaultHelper.loadImage(context, preferenceHelper.getProfilePic(), mBinding.ivProfileImage,
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder),
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder))
        } else mBinding.ivProfileImage.setImageDrawable(
            ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder))

    }

    private fun manageClickEvent() {
        mBinding.txtEditProfile.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.flMain, EditProfileFragment())
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        }
        mBinding.txtProfile.setOnClickListener {
            activity?.onBackPressed()
        }
    }


}