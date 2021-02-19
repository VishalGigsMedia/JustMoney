package com.app.just_money

import android.Manifest.permission
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.just_money.available.AvailableFragment
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.DefaultKeyHelper.availableFragment
import com.app.just_money.common_helper.DefaultKeyHelper.inProgressFragment
import com.app.just_money.common_helper.DefaultKeyHelper.walletFragment
import com.app.just_money.common_helper.OnCurrentFragmentVisibleListener
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.databinding.ActivityMainBinding
import com.app.just_money.in_progress.InProgressFragment
import com.app.just_money.my_wallet.MyWalletFragment
import com.app.just_money.my_wallet.completed.CompletedFragment
import com.app.just_money.my_wallet.faq.FaqFragment
import com.app.just_money.my_wallet.payouts.MyPayoutFragment
import com.app.just_money.my_wallet.setting.SettingFragment
import com.app.just_money.my_wallet.setting.SettingsNewFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import java.util.*

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, OnCurrentFragmentVisibleListener {
    companion object {
        private const val TAG = "MainActivity"
    }

    private var addressList: List<Address>? = null
    private var service: LocationManager? = null
    private var enabled: Boolean? = null
    private var mLocationRequest: LocationRequest? = null
    private var mGoogleApiClient: GoogleApiClient? = null
    private var mLastLocation: Location? = null
    private val updateInterval = (2 * 1000).toLong()  /* 10 secs */
    private val fastestInterval: Long = 2000 /* 2 sec */
    private val requestLocationCode = 1
    private val requestCheckSetting = 2
    private val requestPermissionAllowed = 1

    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        subscribeToTopic()
        getToken()
        //init()
        manageClickEvents()
        //open available fragment
        openFragment(AvailableFragment(), false, availableFragment)
    }

    fun init() {
        try {
            checkLocationPermission()
            service = this.getSystemService(LOCATION_SERVICE) as LocationManager
            enabled = service!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            buildGoogleApiClient()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getToken() {
        // Get token
        // [START log_reg_token]
        Firebase.messaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            //Log.d(TAG, msg)
            Log.d("fcmToken: ", token)

            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })
    }

    override fun onBackPressed() {
        val availableFragment: AvailableFragment? = supportFragmentManager.findFragmentByTag(availableFragment) as AvailableFragment?
        val inProgressFragment: InProgressFragment? = supportFragmentManager.findFragmentByTag(inProgressFragment) as InProgressFragment?
        val walletFragment: MyWalletFragment? = supportFragmentManager.findFragmentByTag(walletFragment) as MyWalletFragment?
        if (availableFragment != null && availableFragment.isVisible ||
            inProgressFragment != null && inProgressFragment.isVisible||
            walletFragment != null && walletFragment.isVisible) {
            val builder = AlertDialog.Builder(this@MainActivity)
            builder.setMessage("Are you sure you want to Exit?").setCancelable(true)
                .setPositiveButton("Yes") { _, _ ->
                    super.onBackPressed()
                }.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }else{
            super.onBackPressed()
        }
    }
    private fun subscribeToTopic() {
        // [START subscribe_topics]
        Firebase.messaging.unsubscribeFromTopic("GENERAL").addOnCompleteListener { task ->
            var msg = getString(R.string.msg_subscribed)
            if (!task.isSuccessful) {
                msg = getString(R.string.msg_subscribe_failed)
            }
            Log.d(TAG, msg)
            //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        // [END subscribe_topics]
    }

    private fun manageClickEvents() {
        mBinding.txtAvailable.setOnClickListener { onClickAvailable() }
        mBinding.txtInProgress.setOnClickListener { onClickInProgress() }
        mBinding.txtMyWallet.setOnClickListener {
            onClickMyWallet()
        }
    }

    private fun onClickAvailable() {
        mBinding.clAvailable.background = ContextCompat.getDrawable(this, R.drawable.curve_blue_grey_900)
        mBinding.clInProgress.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.clMyWallet.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.txtAvailable.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_active_available), null, null, null)
        mBinding.txtMyWallet.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_wallet), null, null, null)
        mBinding.txtInProgress.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_inprogress), null, null, null)
        openFragment(AvailableFragment(), false, availableFragment)
    }

    private fun onClickInProgress() {
        mBinding.clInProgress.background = ContextCompat.getDrawable(this, R.drawable.curve_blue_grey_900)
        mBinding.clAvailable.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.clMyWallet.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.txtAvailable.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_available), null, null, null)
        mBinding.txtMyWallet.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_wallet), null, null, null)
        mBinding.txtInProgress.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_active_inprogress), null, null, null)

        openFragment(InProgressFragment(), false, inProgressFragment)
    }

    fun onClickMyWallet() {
        mBinding.clMyWallet.background = ContextCompat.getDrawable(this, R.drawable.curve_blue_grey_900)
        mBinding.clAvailable.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.clInProgress.setBackgroundColor(ContextCompat.getColor(this, R.color.teal_900))
        mBinding.txtAvailable.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_available), null, null, null)

        mBinding.txtInProgress.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_inactive_inprogress), null, null, null)

        mBinding.txtMyWallet.setCompoundDrawablesWithIntrinsicBounds(
            ContextCompat.getDrawable(this, R.drawable.ic_active_wallet), null, null, null)

        openFragment(MyWalletFragment(), false, walletFragment)
    }

    fun openFragment(fragment: Fragment, addToBackStack: Boolean, tag: String) {
        if (addToBackStack) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction().replace(R.id.flMain, fragment, tag)
                .addToBackStack(MainActivity::class.java.simpleName).commit()
        } else {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction().replace(R.id.flMain, fragment, tag).commit()
        }
    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnected(p0: Bundle?) {
        mLocationRequest = LocationRequest()
        locationGet()
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
        }

    }

    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient =
            GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build()
        mGoogleApiClient!!.connect()
    }

    @SuppressLint("MissingPermission")
    fun getLocation() {
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
            if (mLastLocation == null) {
                // startLocationUpdates();
            }
            if (mLastLocation != null) {
                val geoCoder = Geocoder(this, Locale.ENGLISH)
                addressList = geoCoder.getFromLocation(mLastLocation!!.latitude, mLastLocation!!.longitude, 1)
                // Log.d("lat",mLastLocation!!.latitude.toString())
                if (addressList != null && addressList!!.isNotEmpty()) {
                    val state = addressList!![0].adminArea
                    val city = addressList!![0].locality
                    //println("state : $state  city : $city")
                    val preferenceHelper = PreferenceHelper(this)
                    if (state.isNotEmpty()) {
                        preferenceHelper.setUserState(DefaultHelper.encrypt(state))
                    }
                    if (city.isNotEmpty()) {
                        preferenceHelper.setUserCity(DefaultHelper.encrypt(city))
                    }
                    val availableFragment = AvailableFragment()
                }
            } else {
                //  Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showAlert() {
        mLocationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(updateInterval).setFastestInterval(fastestInterval)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest!!)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {

        }
        task.addOnFailureListener { e ->
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder(this).setTitle(getString(R.string.hint_location_permission_needed))
                    .setMessage(getString(R.string.hint_local_permission_message))
                    .setPositiveButton(getString(R.string.hint_ok)) { dialog, which ->
                        ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_FINE_LOCATION),
                            requestLocationCode)
                    }.create().show()

            } else ActivityCompat.requestPermissions(this, arrayOf(permission.ACCESS_FINE_LOCATION),
                requestLocationCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            requestLocationCode -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the location-related task you need to do.
                    locationGet()
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        shouldShowRequestPermissionRationale(permission.ACCESS_FINE_LOCATION)
                        shouldShowRequestPermissionRationale(permission.ACCESS_COARSE_LOCATION)
                    }
                }
                return
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestCheckSetting && resultCode == Activity.RESULT_OK) {
            locationGet()
        }
    }

    private fun locationGet() {
        try {
            if (!checkGPSEnabled()) {
                showAlert()
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    ) {
                        //Location Permission already granted
                        getLocation()
                    } else {
                        //Request Location Permission
                        checkLocationPermission()

                    }
                } else {
                    getLocation()
                }
            }
        } catch (e: Exception) {

        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled()) showAlert()
        return isLocationEnabled()
    }


    override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient?.isConnected == true) {
            mGoogleApiClient?.disconnect()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            if (mGoogleApiClient!!.isConnected) {
                mGoogleApiClient!!.disconnect()
            }
        } catch (e: Exception) {

        }
    }

    private fun checkAllPermission(): Boolean {
        return checkCallingOrSelfPermission(permission.ACCESS_FINE_LOCATION) !== PackageManager.PERMISSION_GRANTED || checkCallingOrSelfPermission(
            permission.ACCESS_COARSE_LOCATION) !== PackageManager.PERMISSION_GRANTED
    }

    private fun displayNeverAskAgainDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(getString(R.string.dialogMessage))
        builder.setCancelable(false)
        builder.setPositiveButton("Permit Manually") { dialog, which ->
            dialog.dismiss()
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
        builder.show()
    }

    private fun checkRuntimePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this,
                arrayOf(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION),
                requestPermissionAllowed)
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        if (fragment is AvailableFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is InProgressFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is CompletedFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is MyWalletFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is SettingFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is FaqFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is MyPayoutFragment) fragment.setOnCurrentFragmentVisibleListener(this)
        if (fragment is SettingsNewFragment) fragment.setOnCurrentFragmentVisibleListener(this)
    }

    override fun onShowHideBottomNav(show: Boolean) {
        //println("show:  $show")
        if (show) {
            mBinding.bottomNav.visibility = View.VISIBLE
        } else {
            mBinding.bottomNav.visibility = View.GONE
        }
    }


}