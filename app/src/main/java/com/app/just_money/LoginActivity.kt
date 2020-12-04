package com.app.just_money

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.app.just_money.common_helper.DefaultHelper
import com.app.just_money.common_helper.PreferenceHelper
import com.app.just_money.databinding.ActivityLoginBinding
import com.app.just_money.login.LoginFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import java.util.*

class LoginActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    LocationListener {

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
    private lateinit var mBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        init()

        openFragment(LoginFragment(), false)
    }

    private fun openFragment(fragment: Fragment, addToBackStack: Boolean) {
        if (addToBackStack) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.flLogin, fragment)
                .addToBackStack(MainActivity::class.java.simpleName)
                .commit()
        } else {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
            supportFragmentManager.beginTransaction()
                .replace(R.id.flLogin, fragment)
                .commit()
        }
    }


    private fun init() {
        try {

            checkLocationPermission()

            service = this.getSystemService(LOCATION_SERVICE) as LocationManager
            enabled = service!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            buildGoogleApiClient()
        } catch (e: Exception) {
            //e.printStackTrace()
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
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient,
                mLocationRequest,
                this
            )
        }

    }

    @Synchronized
    fun buildGoogleApiClient() {
        mGoogleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()
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
                addressList =
                    geoCoder.getFromLocation(mLastLocation!!.latitude, mLastLocation!!.longitude, 1)
                // Log.d("lat",mLastLocation!!.latitude.toString())
                if (addressList != null && addressList!!.isNotEmpty()) {
                    val state = addressList!![0].adminArea
                    val city = addressList!![0].locality
                    val countryName = addressList!![0].countryName

                    //println("state : $state  city : $city")
                    val preferenceHelper = PreferenceHelper(this)
                    if (state.isNotEmpty()) {
                        preferenceHelper.setUserState(DefaultHelper.encrypt(state))
                    }
                    if (city.isNotEmpty()) {
                        preferenceHelper.setUserCity(DefaultHelper.encrypt(city))
                    }

                    if (countryName.isNotEmpty()) {
                        preferenceHelper.setUserCountry(DefaultHelper.encrypt(countryName))
                    }
                }
            } else {
                //  Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showAlert() {
        mLocationRequest = LocationRequest.create()
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(updateInterval)
            .setFastestInterval(fastestInterval)
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(mLocationRequest!!)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {

        }
        task.addOnFailureListener { e ->
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.hint_location_permission_needed))
                    .setMessage(getString(R.string.hint_local_permission_message))
                    .setPositiveButton(
                        getString(R.string.hint_ok)
                    ) { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            requestLocationCode
                        )
                    }
                    .create()
                    .show()

            } else ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                requestLocationCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
                        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
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
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
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
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkGPSEnabled(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }


    override fun onStart() {
        super.onStart()
        mGoogleApiClient?.connect()
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient!!.isConnected) {
            mGoogleApiClient!!.disconnect()
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

}