package com.app.just_money.common_helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.widget.Toast
import com.app.just_money.BuildConfig
import com.app.just_money.dagger.MyApplication
import org.apache.commons.codec.binary.Base64
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object DefaultHelper {

    fun getBrand(): String {
        return Build.BRAND.toString() + " " + Build.MODEL.toString()
    }

    fun getBuildVersion(): String {
        return Build.VERSION.RELEASE
    }


    fun getVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    fun getVersionCode(): String {
        return BuildConfig.VERSION_CODE.toString()
    }

    fun getCpu(): String {
        return Build.CPU_ABI
    }

    fun getDisplay(): String {
        return Build.DISPLAY
    }

    fun getDeviceType(): String {
        return "phone"
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    //to get network operator name
    fun getCarrierName(context: Context): String {
        val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.networkOperatorName.toString()
    }

    fun isOnline(): Boolean {
        val haveConnectedWifi = false
        val haveConnectedMobile = false
        val cm =
            MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val netInfo = cm.activeNetwork
        if (netInfo != null) {
            val nc = cm.getNetworkCapabilities(netInfo)

            return (nc?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) != null || nc?.hasTransport(
                NetworkCapabilities.TRANSPORT_WIFI
            ) != null);
        }

        return haveConnectedWifi || haveConnectedMobile
    }


    fun getPackageId(context: Context): String {
        var packageId = ""
        try {
            // Get all package signatures for the current package
            val packageName = context.packageName
            val packageManager = context.packageManager
            val signatures = packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            ).signatures

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
            var base64Hash = android.util.Base64.encodeToString(
                hashSignature,
                android.util.Base64.NO_PADDING or android.util.Base64.NO_WRAP
            )
            base64Hash = base64Hash.substring(0, numBase64Char)

            //Log.e(TAG, String.format("pkg: %s -- hash: %s", packageName, base64Hash))
            return base64Hash
        } catch (e: NoSuchAlgorithmException) {
            //Log.e(TAG, "hash:NoSuchAlgorithm", e)
        }

        return null
    }

    fun showToast(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
        var mToast: Toast? = null
        if (message.isNotEmpty()) {
            mToast?.cancel()
            mToast = Toast.makeText(context, message, duration)
            mToast!!.show()
        }
    }

    fun decrypt(plainText: String): String {
        return try {
            val base64 = Base64()
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val secretKey =
                SecretKeySpec(DefaultKeyHelper.secretKey.toByteArray(charset("UTF-8")), "AES")
            val initializeVectorKey = IvParameterSpec(
                DefaultKeyHelper.initializeVectorKey.toByteArray(charset("UTF-8")),
                0,
                cipher.blockSize
            )
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

        val secretKey =
            SecretKeySpec(DefaultKeyHelper.secretKey.toByteArray(charset("UTF-8")), "AES")
        val initializeVectorKey =
            IvParameterSpec(
                DefaultKeyHelper.initializeVectorKey.toByteArray(charset("UTF-8")),
                0,
                cipher.blockSize
            )

        //encrypt
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, initializeVectorKey)
        val encryptedCipherBytes = base64.encode(cipher.doFinal(plainText.toByteArray()))
        return String(encryptedCipherBytes)
    }

}