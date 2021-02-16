package com.app.just_money.my_wallet.setting

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.BuildConfig
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentEditProfileBinding
import com.app.just_money.my_wallet.setting.view_model.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_gender.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class EditProfileFragment : Fragment() {
    @Inject
    lateinit var api: API

    private val idPermissionAllowed = 1
    private var fileProfile: File? = null
    private var fileIsSelected: Boolean = false
    private var picturePath = ""
    private var cal = Calendar.getInstance()
    private lateinit var dialogVerify: Dialog
    private lateinit var viewModel: ProfileViewModel
    private lateinit var mBinding: FragmentEditProfileBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_profile, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        manageClickEvents()
        setData()
    }

    private fun setData() {
        val preferenceHelper = PreferenceHelper(context)
        //Set Name
        mBinding.edtFirstName.setText(preferenceHelper.getFirstName())
        mBinding.edtLastName.setText(preferenceHelper.getLastName())
        //Set DOB
        if (preferenceHelper.getDob().contains("0000")) {
            //it means dob not set yet
            mBinding.edtBirthDate.setText("")
        } else {
            mBinding.edtBirthDate.setText(preferenceHelper.getDob())
        }
        //Set Gender
        when (preferenceHelper.getGender()) {
            "0" -> {
                mBinding.edtGender.setText("")
            }
            DefaultKeyHelper.male -> {
                mBinding.edtGender.setText("Male")
            }
            DefaultKeyHelper.female -> {
                mBinding.edtGender.setText("Female")
            }
        }
        //Set Email & Image
        mBinding.txtEmail.text = preferenceHelper.getEmail()
        DefaultHelper.loadImage(context, preferenceHelper.getProfilePic(), mBinding.ivProfileImage,
            ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder)!!,
            ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder)!!)
        mBinding.edtEmail.setText(preferenceHelper.getEmail())
        val profilePic = DefaultHelper.decrypt(preferenceHelper.getProfilePic())
        if (profilePic.isNotEmpty() && profilePic != "null") {
            DefaultHelper.loadImage(context, preferenceHelper.getProfilePic(), mBinding.ivProfileImage,
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder)!!,
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder)!!)
        } else {
            mBinding.ivProfileImage.setImageDrawable(
                ContextCompat.getDrawable(context!!, R.drawable.ic_user_place_holder))
        }


    }

    private fun manageClickEvents() {
        mBinding.ivProfileImage.setOnClickListener {
            onRequestPermission()
        }

        cal.add(Calendar.YEAR, -18)
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        mBinding.edtBirthDate.setOnClickListener {
            selectDate(dateSetListener)
        }

        mBinding.edtGender.setOnClickListener {
            showGenderDialog()
        }

        mBinding.txtUpdateProfile.setOnClickListener {
            onClickUpdateProfile()
        }

        mBinding.txtEditProfile.setOnClickListener{
            activity?.onBackPressed()
        }
    }

    private fun updateDateInView() {
        try {
            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            val time = sdf.format(cal.time)
            mBinding.edtBirthDate.setText(time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun selectDate(dateSetListener: DatePickerDialog.OnDateSetListener) {
        val datePickerDialog =
            DatePickerDialog(activity!!, R.style.calender, dateSetListener, cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun onClickUpdateProfile() {
        val firstName = mBinding.edtFirstName.text.toString()
        val lastName = mBinding.edtLastName.text.toString()
        var gender = "0"
        if (mBinding.edtGender.text.toString() == "Male"){
            gender = DefaultKeyHelper.male
        }else if(mBinding.edtGender.text.toString() == "Female"){
            gender = DefaultKeyHelper.female
        }
        val dob = mBinding.edtBirthDate.text.toString()
        val email = mBinding.txtEmail.text.toString()
        Log.d("jkhbdekjb", "onClickUpdateProfile: $gender")
        viewModel.updateProfile(context!!, api, firstName, lastName, dob, gender, email, fileProfile)
            .observe(viewLifecycleOwner, { updateProfileModel ->
                if (updateProfileModel != null) {
                    when {
                        updateProfileModel.status == DefaultKeyHelper.successCode -> {
                            DefaultHelper.showToast(context!!, DefaultHelper.decrypt(updateProfileModel.message.toString()))

                            //update preferences
                            val preferenceHelper =PreferenceHelper(context)
                            preferenceHelper.setFirstName(firstName)
                            preferenceHelper.setLastName(lastName)
                            preferenceHelper.setDob(dob)
                            preferenceHelper.setGender(gender)

                            activity?.onBackPressed()
                        }
                        updateProfileModel.status == DefaultKeyHelper.failureCode -> {
                            DefaultHelper.showToast(context,
                                DefaultHelper.decrypt(updateProfileModel.message.toString()))
                        }
                        updateProfileModel.forceLogout != 0 -> {

                        }
                    }
                }
            })
    }


    private fun onRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA), idPermissionAllowed)
        } else {
            selectImage()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == idPermissionAllowed && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // CommonHelper.hideKeyboard(activity!!)
            selectImage()
        }
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(context!!)
        builder.setItems(options) { dialog: DialogInterface, item: Int ->
            when {
                options[item] == "Take Photo" -> {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val f = File(Environment.getExternalStorageDirectory(), "temp.jpg")
                    val imageUri: Uri
                    imageUri = FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", f)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(intent, 1)
                }
                options[item] == "Choose from Gallery" -> {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, 2)
                }
                options[item] == "Cancel" -> {
                    dialog.dismiss()
                }
            }
        }
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                fileProfile = File(Environment.getExternalStorageDirectory().toString())
                if (fileProfile != null) {
                    for (temp in fileProfile!!.listFiles()) {
                        if (temp.name == "temp.jpg") {
                            fileProfile = temp
                            break
                        }
                    }
                    val bitmap: Bitmap
                    val bitmapOptions = BitmapFactory.Options()
                    bitmap = BitmapFactory.decodeFile(fileProfile!!.absolutePath, bitmapOptions)
                    mBinding.ivProfileImage.setImageBitmap(bitmap)
                    fileIsSelected = true
                } else if (requestCode == 2) {
                    val selectedImage = data!!.data
                    val filePath = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = activity!!.contentResolver.query(selectedImage!!, filePath, null, null, null)
                    cursor!!.moveToFirst()
                    val columnIndex = cursor.getColumnIndex(filePath[0])
                    picturePath = cursor.getString(columnIndex)
                    cursor.close()

                    val thumbnail = BitmapFactory.decodeFile(picturePath)
                    mBinding.ivProfileImage.setImageBitmap(thumbnail)
                    mBinding.ivProfileImage.buildLayer()
                    fileProfile = File(picturePath)
                    fileIsSelected = true
                }
            }
        }
                if (fileProfile != null) {
                    val myBitmap = BitmapFactory.decodeFile(fileProfile!!.absolutePath)
                    //println("fileProfile : $myBitmap")
                    val bitmap = modifyOrientation(myBitmap, fileProfile!!.absolutePath)
                    mBinding.ivProfileImage.setImageBitmap(bitmap)
                    fileIsSelected = true
                }
            } else if (requestCode == 2) {
                val selectedImage = data!!.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = activity!!.contentResolver.query(selectedImage!!, filePath, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePath[0])
                picturePath = cursor.getString(columnIndex)
                //println("picturePath : $picturePath")
                cursor.close()
                // val myBitmap = BitmapFactory.decodeFile(picturePath)

                fileProfile = File(picturePath)
                val myBitmap = BitmapFactory.decodeFile(fileProfile!!.absolutePath)
                val bitmap = modifyOrientation(myBitmap, picturePath)
                mBinding.ivProfileImage.setImageBitmap(bitmap)
                mBinding.ivProfileImage.buildLayer()
                fileIsSelected = true
                //rotateImage(mBinding.ivEditImage)
            }
        }
    }


    @Throws(IOException::class)
    fun modifyOrientation(bitmap: Bitmap, image_absolute_path: String?): Bitmap? {
        val ei = ExifInterface(image_absolute_path.toString())
        return when (ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate(bitmap, 270f)
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> flip(bitmap, horizontal = true, vertical = false)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> flip(bitmap, horizontal = false, vertical = true)
            else -> bitmap
        }
    }

    private fun rotate(bitmap: Bitmap, degrees: Float): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degrees)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun flip(bitmap: Bitmap, horizontal: Boolean, vertical: Boolean): Bitmap? {
        val matrix = Matrix()
        matrix.preScale((if (horizontal) -1 else 1.toFloat()) as Float,
            (if (vertical) -1 else 1.toFloat()) as Float)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */)
        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = image.absolutePath
        //println("mCurrentPhotoPath: $mCurrentPhotoPath")
        return image
    }

    private fun showGenderDialog() {
        val dialog = BottomSheetDialog(context!!, R.style.AppBottomSheetDialogTheme)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_gender)
        dialog.window?.setGravity(Gravity.BOTTOM)
        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window?.setLayout(width, height)
        dialog.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = group.findViewById(checkedId)
            mBinding.edtGender.setText(radio.text.toString())
            dialog.dismiss()
        }
        dialog.show()
    }

}