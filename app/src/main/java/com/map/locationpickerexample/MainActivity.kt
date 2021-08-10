package com.map.locationpickerexample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.map.locationpicker.CircleData
import com.map.locationpicker.Constants
import com.map.locationpicker.MapType
import com.map.locationpicker.LocationPicker


class MainActivity : AppCompatActivity() {

    val PERMISSIONS_REQUEST_FINE_LOCATION=601
    var lat:Double=0.0
    var long:Double=0.0
    var radius:Double=0.0

    fun isFineLocationPermissionGranted(): Boolean {
        if (
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED  ) {

            ActivityCompat.requestPermissions(this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                PERMISSIONS_REQUEST_FINE_LOCATION
            )

            return false
        } else {
            // Permission has already been granted
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val gpsTracker = GpsTracker(this@MainActivity)
        val applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)

        findViewById<Button>(R.id.open_place_picker_button).setOnClickListener {
            if(isFineLocationPermissionGranted()){
                gpsTracker.getCurrentLocation()
                val latitude: Double = gpsTracker.getLatitude()
                val longitude: Double = gpsTracker.getLongitude()

                val intent = LocationPicker.IntentBuilder()
                    .setLatLong(latitude, longitude)
                    .setMapRawResourceStyle(R.raw.map_style)
                    .setMapType(MapType.NORMAL)
                    .setPlaceSearchBar(applicationInfo.metaData.getString("com.google.android.geo.API_KEY"))
                    .setMarkerImageImageColor(R.color.bleu)
                    .setFabColor(R.color.bleu)
                    .setPrimaryTextColor(R.color.black)
                    .setCircleBackgroundColorRes(R.color.bleu_light)
                    .setSliderThumbTintColor(R.color.bleu)
                    .setSliderTrackActiveTintColor(R.color.bleu)
                    .setSliderTrackInactiveTintColor(R.color.dark)
                    .setConfirmButtonBackgroundShape(R.drawable.round_corner_bleu)
                    .setConfirmButtonTextColor(R.color.white)
                    .setBottomViewColor(R.color.white)
                    .setConfirmButtonText("Confirm")
                    .setInitialCircleRadiusKilometer(10.0)
                    .setSliderValueFrom(1.0F)
                    .setSliderValueTo(200.0F)
                        .enableMapGesturesEnabled(true)
                    .hideLocationButton(false)
                    .build(this)
                startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST)
            }else{
                isFineLocationPermissionGranted()
            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    val circleData = data?.getParcelableExtra<CircleData>(
                        Constants.CIRCLE_INTENT)
                    lat=circleData!!.latitude
                    long=circleData!!.longitude
                    radius=circleData!!.radius
                    findViewById<TextView>(R.id.address_data_text_view).text = circleData.toString()
                } catch (e: Exception) {
                    Log.e("MainActivity", e.message)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
