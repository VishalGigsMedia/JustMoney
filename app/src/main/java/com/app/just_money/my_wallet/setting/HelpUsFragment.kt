package com.app.just_money.my_wallet.setting

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.dagger.API
import com.app.just_money.dagger.MyApplication
import com.app.just_money.databinding.FragmentHelpUsBinding
import java.io.File
import java.io.IOException
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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_help_us, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.instance.getNetComponent()?.inject(this)
        viewModel = ViewModelProvider(this).get(HelpViewModel::class.java)
        setData()
        manageClickEvent()
    }

    private fun setData() {
        val preferenceHelper = PreferenceHelper(context)
        mBinding.edtName.setText("${preferenceHelper.getFirstName()} ${preferenceHelper.getLastName()}")
        mBinding.edtEmail.setText(preferenceHelper.getEmail())
    }

    private fun manageClickEvent() {
        mBinding.ivUploadImage.setOnClickListener { onRequestPermission() }
        mBinding.txtSendFeedback.setOnClickListener { if (validate()) sendFeedback() }
        mBinding.txtHelpUs.setOnClickListener { activity?.onBackPressed() }
    }

    private fun validate(): Boolean {
        when {
            mBinding.edtName.text.toString().isEmpty() -> {
                DefaultHelper.showToast(context!!, getString(R.string.ent_name))
                return false
            }
            mBinding.edtEmail.text.toString().isEmpty() -> {
                DefaultHelper.showToast(context!!, getString(R.string.ent_email))
                return false
            }
            mBinding.edtSubject.text.toString().isEmpty() -> {
                DefaultHelper.showToast(context!!, getString(R.string.ent_subject))
                return false
            }
            mBinding.edtDescription.text.toString().isEmpty() -> {
                DefaultHelper.showToast(context!!, getString(R.string.ent_describe))
                return false
            }
            else -> return true
        }
    }

    private fun onRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA), idPermissionAllowed)
        } else {
            // DefaultHelper.hideKeyboard(activity!!)
            selectImage()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        println("requestCode: $requestCode")
        if (requestCode == idPermissionAllowed && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // CommonHelper.hideKeyboard(activity!!)
            selectImage()
        }
    }

    private fun selectImage() {
        if (context == null) return
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder = AlertDialog.Builder(context!!)
        builder.setItems(options) { dialog: DialogInterface, item: Int ->
            when {
                options[item] == "Take Photo" -> {
                    try {
                        fileProfile = createImageFile()
                        // Continue only if the File was successfully created
                        if (fileProfile != null) {
                            val photoURI =
                                FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider",
                                    fileProfile!!)
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
                if (fileProfile != null) {
                    val myBitmap = BitmapFactory.decodeFile(fileProfile?.absolutePath)
                    val bitmap = modifyOrientation(myBitmap, fileProfile?.absolutePath)
                    mBinding.ivUploadImage.setImageBitmap(bitmap)
                    fileIsSelected = true
                }
            } else if (requestCode == 2) {
                val selectedImage = data!!.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = activity?.contentResolver?.query(selectedImage!!, filePath, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePath[0])
                picturePath = cursor.getString(columnIndex)
                //println("picturePath : $picturePath")
                cursor.close()
                // val myBitmap = BitmapFactory.decodeFile(picturePath)

                fileProfile = File(picturePath)
                val myBitmap = BitmapFactory.decodeFile(fileProfile?.absolutePath)
                val bitmap = modifyOrientation(myBitmap, picturePath)
                mBinding.ivUploadImage.setImageBitmap(bitmap)
                mBinding.ivUploadImage.buildLayer()
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

    private fun sendFeedback() {
        mBinding.txtSendFeedback.isEnabled = false
        mBinding.progress.visibility = VISIBLE
        val name = mBinding.edtName.text.toString()
        val email = mBinding.edtEmail.text.toString()
        val subject = mBinding.edtSubject.text.toString()
        val message = mBinding.edtDescription.text.toString()
        viewModel.sendFeedback(context!!, api, name, email, subject, message, fileProfile)
            .observe(viewLifecycleOwner, { sendFeedbackModel ->
                mBinding.txtSendFeedback.isEnabled = true
                mBinding.progress.visibility = GONE
                if (sendFeedbackModel != null) {
                    when {
                        sendFeedbackModel.status == DefaultKeyHelper.successCode -> {
                            DefaultHelper.showToast(context,
                                DefaultHelper.decrypt(sendFeedbackModel.message.toString()))
                            activity?.onBackPressed()
                        }
                        sendFeedbackModel.status == DefaultKeyHelper.failureCode -> {
                            DefaultHelper.showToast(context,
                                DefaultHelper.decrypt(sendFeedbackModel.message.toString()))
                        }
                        sendFeedbackModel.forceLogout != 0 -> {
                            DefaultHelper.showToast(context,
                                DefaultHelper.decrypt(sendFeedbackModel.message.toString()))
                        }
                        else -> {
                            DefaultHelper.showToast(context,
                                DefaultHelper.decrypt(sendFeedbackModel.message.toString()))
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
        val image = File.createTempFile(imageFileName, /* prefix */
            ".jpg", /* suffix */
            storageDir      /* directory */)
        // Save a file: path for use with ACTION_VIEW intents
        val mCurrentPhotoPath = image.absolutePath
        //println("mCurrentPhotoPath: $mCurrentPhotoPath")
        return image
    }


}