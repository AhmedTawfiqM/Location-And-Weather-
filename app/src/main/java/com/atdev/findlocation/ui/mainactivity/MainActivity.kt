package com.atdev.findlocation.ui.mainactivity

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atdev.findlocation.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener {

    lateinit var viewModel: MainViewModel
    val REQUEST_CODE = 21212
    val PLaySERVICE_Request = 32

    lateinit var googleApiClient: GoogleApiClient
    lateinit var locationRequest: LocationRequest

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //

        checkPermissions()

        if (checkPermissions())
            buildLocationNow()

        //
    }


    private fun observerWeatherAPI() {

        //viewModel.setLatLon(12.21, 213.1) //for First Initial
        viewModel.rootObject.observe(this, Observer { rootObject ->
            //..
            Toast.makeText(this, rootObject.weather[0].toString(), Toast.LENGTH_LONG).show()
            tvWeather.text =
                "${rootObject.coord} \n ${rootObject.weather[0]} \n ${rootObject.main} \n ${rootObject.wind}"
        })
//

    }

    private fun checkPermissions(): Boolean {

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ), REQUEST_CODE
                )
            } //

            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {

            REQUEST_CODE -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                } else {
                    //Granted..

                    if (checkPlaySerice()) {

                        buildLocationNow()
                    }
                }
            }
        }
    }

    private fun buildLocationNow() {


        googleApiClient = GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API).build()
    }

    private fun checkPlaySerice(): Boolean {

        val resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {

            if (GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)) {

                GoogleApiAvailability.getInstance()
                    .getErrorDialog(this, resultCode, PLaySERVICE_Request).show()
            } else {

                Toast.makeText(this, "this Device is not Supported", Toast.LENGTH_LONG).show()
                finish()
            }
            return false
        }

        return true
    }

    override fun onConnected(p0: Bundle?) {

        createLocationRequest()
    }

    @SuppressLint("RestrictedApi")
    private fun createLocationRequest() {

        locationRequest = LocationRequest()
        locationRequest.interval = 10000         //10 sec
        locationRequest.fastestInterval = 5000   // 5 sec
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        if (!checkPermissions()) {

            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let { it: Location ->
                // Logic to handle location object

                SetLocationAndWeather(location)
                //get Weather By Lat and Log request Location
                observerWeatherAPI()


            } ?: kotlin.run {
                // Handle Null case or Request periodic location update
                // https://developer.android.com/training/location/receive-location-updates
            }
        }
    }

    private fun SetLocationAndWeather(location: Location?) {

        tvLocation.text = "lat:  ${location!!.latitude} lon: ${location.longitude}"
        viewModel.setLatLon(location!!.latitude, location.longitude)

    }

    override fun onConnectionSuspended(p0: Int) {

        googleApiClient.connect()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, p0.errorMessage, Toast.LENGTH_LONG).show()

    }

    override fun onLocationChanged(location: Location?) {

        SetLocationAndWeather(location)
        //viewModel.setLatLon(location!!.latitude,location.longitude)

    }

    override fun onStart() {
        super.onStart()
        googleApiClient.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        googleApiClient.disconnect()
    }

    override fun onResume() {
        super.onResume()
        checkPlaySerice()
    }
}
