package com.app.just_money.my_wallet.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.BundleHelper
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentProfileBinding
import com.app.just_money.my_wallet.setting.view_model.ProfileViewModel
import com.bumptech.glide.Glide
import javax.inject.Inject

class MyProfileFragment : Fragment() {
    @Inject
    lateinit var api: API

    private var name = ""
    private var lastName = ""
    private var dob = ""
    private var gender = ""
    private var email = ""
    private var imageUrl = ""
    private lateinit var viewModel: ProfileViewModel
    private lateinit var mBinding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        manageClickEvent()
        getProfile()
    }

    private fun manageClickEvent() {
        mBinding.txtEditProfile.setOnClickListener {
            onClickEditProfile()
        }

    }

    private fun onClickEditProfile() {
        name = mBinding.txtNameValue.text.toString()
        dob = mBinding.txtBirthDateValue.text.toString()
        gender = mBinding.txtGenderValue.text.toString()
        email = mBinding.txtEmailValue.text.toString()

        val editProfileFragment = EditProfileFragment()
        val bundle = Bundle()
        bundle.putString(BundleHelper.name, name)
        bundle.putString(BundleHelper.lastName, DefaultHelper.decrypt(lastName))
        bundle.putString(BundleHelper.dob, dob)
        bundle.putString(BundleHelper.gender, gender)
        bundle.putString(BundleHelper.email, email)
        bundle.putString(BundleHelper.imageUrl, imageUrl)
        editProfileFragment.arguments = bundle
        openFragment(editProfileFragment, true)
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, fragment)
                .addToBackStack(MainActivity::class.java.simpleName)
                .commit()
        } else {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.flMain, fragment)
                .commit()
        }
    }

    private fun getProfile() {
        viewModel.getUserProfile(context!!, api).observe(viewLifecycleOwner, { getUserProfile ->
            if (getUserProfile != null) {
                when {
                    getUserProfile.status == DefaultKeyHelper.successCode -> {
                        if (getUserProfile.userProfileData != null) {
                            name = getUserProfile.userProfileData.firstname.toString()
                            lastName = getUserProfile.userProfileData.lastname.toString()
                            dob = getUserProfile.userProfileData.dob.toString()
                            gender = getUserProfile.userProfileData.gender.toString()
                            email = getUserProfile.userProfileData.email.toString()
                            imageUrl = getUserProfile.userProfileData.profilePic.toString()
                            if (dob.isNotEmpty()) {
                                mBinding.txtBirthDateValue.text = DefaultHelper.decrypt(dob)
                            }
                            if (name.isNotEmpty()) {
                                val fullName =
                                    DefaultHelper.decrypt(name) + " " +
                                            DefaultHelper.decrypt(lastName)
                                mBinding.txtNameValue.text = fullName
                            }
                            if (email.isNotEmpty()) {
                                mBinding.txtEmailValue.text = DefaultHelper.decrypt(email)
                            }
                            if (gender.isNotEmpty()) {
                                val genderValue = DefaultHelper.decrypt(gender)
                                if (genderValue == "0") {
                                    mBinding.txtGenderValue.text =
                                        context!!.getString(R.string.female)
                                } else if (genderValue == "1") {
                                    mBinding.txtGenderValue.text =
                                        context!!.getString(R.string.male)
                                }
                            }
                            if (imageUrl.isNotEmpty()) {
                                Glide.with(context!!)
                                    .load(imageUrl)
                                    .placeholder(R.drawable.ic_user_place_holder)
                                    .error(R.drawable.ic_user_place_holder)
                                    .into(mBinding.ivProfileImage)
                            }
                        }
                    }
                    getUserProfile.status == DefaultKeyHelper.failureCode -> {

                    }
                    getUserProfile.forceLogout != 0 -> {

                    }
                }
            }
        })
    }


}