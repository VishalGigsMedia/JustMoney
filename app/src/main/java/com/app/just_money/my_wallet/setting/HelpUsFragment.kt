package com.app.just_money.my_wallet.setting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.app.just_money.BuildConfig
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentHelpUsBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class HelpUsFragment : Fragment() {

    @Inject
    lateinit var api: API

    private val idPermissionAllowed = 1
    private var fileProfile: File? = null
    private var fileIsSelected: Boolean = false
    private var picturePath = ""

    private lateinit var viewModel: HelpViewModel
    private lateinit var mBinding: FragmentHelpUsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_help_us, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(HelpViewModel::class.java)
        manageClickEvent()
    }

    private fun manageClickEvent() {
        mBinding.txtUploadImage.setOnClickListener {
            onRequestPermission()
        }
        mBinding.txtSendFeedback.setOnClickListener {
            if (validate()) {
                sendFeedback()
            }
        }
    }

    private fun validate(): Boolean {
        val name = mBinding.edtName.text.toString()
        val email = mBinding.edtEmail.text.toString()
        val subject = mBinding.edtSubject.text.toString()
        val message = mBinding.edtDescription.text.toString()
        when {
            name.isEmpty() -> {
                DefaultHelper.showToast(context!!, getString(R.string.ent_name))
                return false
            }
            email.isEmpty() -> {
                DefaultHelper.showToast(context!!, getString(R.string.ent_email))
                return false
            }
            subject.isEmpty() -> {
                DefaultHelper.showToast(context!!, getString(R.string.ent_subject))
                return false
            }
            message.isEmpty() -> {
                DefaultHelper.showToast(context!!, getString(R.string.ent_describe))
                return false
            }
            else -> return true
        }
    }

    private fun onRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                idPermissionAllowed
            )
        } else {
            // DefaultHelper.hideKeyboard(activity!!)
            selectImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("requestCode: $requestCode")
        if (requestCode == idPermissionAllowed && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // CommonHelper.hideKeyboard(activity!!)
            selectImage()
        }
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(
            context!!
        )
        builder.setItems(options) { dialog: DialogInterface, item: Int ->
            when {
                options[item] == "Take Photo" -> {
                    try {
                        fileProfile = createImageFile()
                        // Continue only if the File was successfully created
                        if (fileProfile != null) {
                            val photoURI = FileProvider.getUriForFile(
                                context!!,
                                BuildConfig.APPLICATION_ID + ".provider",
                                fileProfile!!
                            )
                            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                            startActivityForResult(takePictureIntent, 1)
                        }
                    } catch (ex: Exception) {
                        // Error occurred while creating the File
                        //displayMessage(baseContext, ex.message.toString())
                    }
                }
                options[item] == "Choose from Gallery" -> {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
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
                val myBitmap = BitmapFactory.decodeFile(fileProfile!!.absolutePath)
                //println("fileProfile : $myBitmap")
                mBinding.ivUploadImage.setImageBitmap(myBitmap)
            } else if (requestCode == 2) {
                val selectedImage = data!!.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor =
                    activity!!.contentResolver.query(selectedImage!!, filePath, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePath[0])
                picturePath = cursor.getString(columnIndex)
                cursor.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
                mBinding.ivUploadImage.setImageBitmap(thumbnail)
                mBinding.ivUploadImage.buildLayer()
                fileProfile = File(picturePath)
                fileIsSelected = true
            }
        }
    }

    private fun sendFeedback() {
        val name = mBinding.edtName.text.toString()
        val email = mBinding.edtEmail.text.toString()
        val subject = mBinding.edtSubject.text.toString()
        val message = mBinding.edtDescription.text.toString()
        viewModel.sendFeedback(context!!, api, name, email, subject, message, fileProfile)
            .observe(viewLifecycleOwner,
                { sendFeedbackModel ->
                    if (sendFeedbackModel != null) {
                        when (sendFeedbackModel.status) {
                            DefaultKeyHelper.successCode -> {
                                DefaultHelper.showToast(
                                    context!!,
                                    DefaultHelper.decrypt(sendFeedbackModel.message.toString())
                                )
                            }
                            DefaultKeyHelper.failureCode -> {
                                DefaultHelper.showToast(
                                    context!!,
                                    DefaultHelper.decrypt(sendFeedbackModel.message.toString())
                                )
                            }
                            DefaultKeyHelper.forceLogoutCode -> {
                                DefaultHelper.showToast(
                                    context!!,
                                    DefaultHelper.decrypt(sendFeedbackModel.message.toString())
                                )
                            }
                            else -> {
                                DefaultHelper.showToast(
                                    context!!,
                                    DefaultHelper.decrypt(sendFeedbackModel.message.toString())
                                )
                            }
                        }
                    }
                })
    }


    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */
        )
        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = image.absolutePath
        //println("mCurrentPhotoPath: $mCurrentPhotoPath")
        return image
    }


}