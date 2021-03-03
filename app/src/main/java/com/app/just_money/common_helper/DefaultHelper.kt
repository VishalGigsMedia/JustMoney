package com.app.just_money.common_helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Patterns
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.app.just_money.BuildConfig
import com.app.just_money.LoginActivity
import com.app.just_money.MainActivity
import com.app.just_money.R
import com.app.just_money.common_helper.DefaultKeyHelper.FACEBOOK
import com.app.just_money.dagger.MyApplication
import com.bumptech.glide.Glide
import org.apache.commons.codec.binary.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object DefaultHelper {

    fun getBrand(): String = Build.BRAND.toString() + " " + Build.MODEL.toString()

    fun getBuildVersion(): String = Build.VERSION.RELEASE

    fun getVersionName(): String = BuildConfig.VERSION_NAME

    fun getVersionCode(): Long = BuildConfig.VERSION_CODE.toLong()

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    //to get network operator name
    fun getCarrierName(context: Context?): String {
        val tm = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkOperatorName.toString()
    }

    fun isOnline(): Boolean {
        val haveConnectedWifi = false
        val haveConnectedMobile = false
        val cm = MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val netInfo = cm.activeNetwork
        if (netInfo != null) {
            val nc = cm.getNetworkCapabilities(netInfo)
            return (nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) != null || nc?.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI) != null)
        }

        return haveConnectedWifi || haveConnectedMobile
    }


    fun getPackageId(context: Context): String {
        var packageId = ""
        try {
            // Get all package signatures for the current package
            val packageName = context.packageName
            val packageManager = context.packageManager
            val signatures = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures

            // For each signature create a compatible hash
            for (signature in signatures) {
                println("packageName: $packageName \n signature: " + signature.toCharsString())
                packageId = hash(packageName, signature.toCharsString())!!
            }
        } catch (e: PackageManager.NameNotFoundException) {
            //Log.e(TAG, "Unable to find package to obtain hash.", e)
        }
        return packageId
    }

    private fun hash(packageName: String, signature: String): String? {
        val hashType = "SHA-256"
        val numHashedBytes = 9
        val numBase64Char = 11
        val appInfo = "$packageName $signature"
        try {
            val messageDigest = MessageDigest.getInstance(hashType)
            messageDigest.update(appInfo.toByteArray(StandardCharsets.UTF_8))
            var hashSignature = messageDigest.digest()

            // truncated into NUM_HASHED_BYTES
            hashSignature = Arrays.copyOfRange(hashSignature, 0, numHashedBytes)
            // encode into Base64
            var base64Hash = android.util.Base64.encodeToString(hashSignature,
                android.util.Base64.NO_PADDING or android.util.Base64.NO_WRAP)
            base64Hash = base64Hash.substring(0, numBase64Char)

            //Log.e(TAG, String.format("pkg: %s -- hash: %s", packageName, base64Hash))
            return base64Hash
        } catch (e: NoSuchAlgorithmException) {
            //Log.e(TAG, "hash:NoSuchAlgorithm", e)
        }

        return null
    }

    fun showToast(context: Context?, message: String?, duration: Int = Toast.LENGTH_LONG) {
        var mToast: Toast? = null
        if (message.toString().isNotEmpty()) {
            mToast?.cancel()
            mToast = Toast.makeText(context, message, duration)
            mToast!!.show()
        }
    }

    fun decrypt(plainText: String): String {
        return try {
            val base64 = Base64()
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val secretKey = SecretKeySpec(DefaultKeyHelper.secretKey.toByteArray(charset("UTF-8")), "AES")
            val initializeVectorKey =
                IvParameterSpec(DefaultKeyHelper.initializeVectorKey.toByteArray(charset("UTF-8")), 0,
                    cipher.blockSize)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, initializeVectorKey)
            //decrypt
            val text = cipher.doFinal(base64.decode(plainText.toByteArray()))
            //println("Decrypt text : " + String(text))
            String(text)
        } catch (e: Exception) {
            plainText
        }
    }


    fun encrypt(plainText: String): String {
        val base64 = Base64()
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")

        val secretKey = SecretKeySpec(DefaultKeyHelper.secretKey.toByteArray(charset("UTF-8")), "AES")
        val initializeVectorKey =
            IvParameterSpec(DefaultKeyHelper.initializeVectorKey.toByteArray(charset("UTF-8")), 0,
                cipher.blockSize)

        //encrypt
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, initializeVectorKey)
        val encryptedCipherBytes = base64.encode(cipher.doFinal(plainText.toByteArray()))
        return String(encryptedCipherBytes)
    }

    fun forceLogout(context: FragmentActivity?) {
        val intent = Intent(context, LoginActivity::class.java)
        context?.startActivity(intent)
        context?.finish()
    }

    fun isValidEmailId(emailId: String): Boolean {
        return !TextUtils.isEmpty(emailId) && Patterns.EMAIL_ADDRESS.matcher(emailId).matches()
    }

    fun openFacebookPage(context: Context?) {
        if (isPackageInstalled(context, FACEBOOK)) {
            context?.startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/${DefaultKeyHelper.facebookPageId}")))
        } else {
            context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(DefaultKeyHelper.facebookPageUrl)))
        }
    }

    private fun isPackageInstalled(c: Context?, targetPackage: String): Boolean {
        val pm = c?.packageManager
        try {
            val info = pm?.getPackageInfo(targetPackage, PackageManager.GET_META_DATA)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        }
        return true
    }

    fun openFragment(supportFragmentManager: FragmentManager?, fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            //activity?.supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager?.beginTransaction()?.replace(R.id.flLogin, fragment)
                ?.addToBackStack(MainActivity::class.java.simpleName)?.commit()
        } else {
            supportFragmentManager?.beginTransaction()?.replace(R.id.flLogin, fragment)?.commit()
        }
    }

    fun loadImage(context: Context?, imageUrl: String, imageView: ImageView,
        placeholder: Drawable? = ContextCompat.getDrawable(context!!, R.drawable.ic_logo),
        error: Drawable? = ContextCompat.getDrawable(context!!, R.drawable.ic_logo)) {
        context?.let {
            Glide.with(it).load(imageUrl).placeholder(placeholder).error(error).into(imageView)
        }
    }

    fun playNotificationSound(context: Context?) {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(context, notification)
            r.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun playCustomSound(context: Context?, item: Int) {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(context, item)
        mediaPlayer.start()
    }

    fun share(sharingText: String?, context: Context?, appPackage: String) {
        val i = Intent(Intent.ACTION_SEND)
        if (isPackageInstalled(context, appPackage)) i.setPackage(appPackage)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, context?.getString(R.string.app_name))
        i.putExtra(Intent.EXTRA_TEXT, sharingText)
        context?.startActivity(Intent.createChooser(i, "${context.getString(R.string.app_name)} Share"))
    }
}