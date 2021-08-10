package com.map.locationpicker

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.Slider
import com.google.maps.android.SphericalUtil
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.ln

class LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback {

  companion object {
    private const val TAG = "PlacePickerActivity"
  }

  private lateinit var map: GoogleMap
  private var googleApiKey: String? = null

  private lateinit var markerImage: ImageView
  private lateinit var placeSelectedFab: FloatingActionButton
  private lateinit var myLocationFab: FloatingActionButton
  private lateinit var distanceSlider: Slider
  private lateinit var sliderValueTextView: TextView
  private lateinit var infoLayout: FrameLayout

  private var latitude = Constants.DEFAULT_LATITUDE
  private var longitude = Constants.DEFAULT_LONGITUDE
  private var circleRadiusKilometer:Double=
      Constants.DEFAULT_CIRCLE_RADIUS_INTENT
  private var initLatitude = Constants.DEFAULT_LATITUDE
  private var initLongitude = Constants.DEFAULT_LONGITUDE
  private var markerColorRes: Int = -1
  private var circleColorRes: Int = -1
  private var fabColorRes: Int = -1
  private var primaryTextColorRes: Int = -1
  private var bottomViewColorRes: Int = -1
  private var mapRawResourceStyleRes: Int = -1
  private var mapType: MapType =
      MapType.NORMAL
  private var hideLocationButton: Boolean = false
  private var sliderThumbTintColorRes: Int = -1
  private var sliderTrackInactiveTintColorRes: Int = -1
  private var sliderTrackActiveTintColorRes: Int = -1
  private var initCircleRadiusKilometer:Double=
      Constants.DEFAULT_CIRCLE_RADIUS_INTENT
  private var sliderValueFrom:Float=
      Constants.DEFAULT_SLIDER_VALUE_FROM_INTENT
  private var sliderValueTo:Float=
      Constants.DEFAULT_SLIDER_VALUE_TO_INTENT

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_place_picker)
    getIntentData()

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    val mapFragment = supportFragmentManager
        .findFragmentById(R.id.map_fragment) as SupportMapFragment
    mapFragment.getMapAsync(this)
    bindViews()

    placeSelectedFab.setOnClickListener {
      val circleData= CircleData(
          latitude,
          longitude,
          circleRadiusKilometer
      )
      val returnIntent = Intent()
      returnIntent.putExtra(Constants.CIRCLE_INTENT, circleData)
      setResult(RESULT_OK, returnIntent)
      finish()
    }

    myLocationFab.setOnClickListener {
      if(this::map.isInitialized) {

        val targetNorthEast: LatLng =
          SphericalUtil.computeOffset(LatLng(initLatitude, initLongitude), circleRadiusKilometer * 1000 * Math.sqrt(2.0), 45.0)
        val targetSouthWest: LatLng =
          SphericalUtil.computeOffset(LatLng(initLatitude, initLongitude), circleRadiusKilometer * 1000 * Math.sqrt(2.0), 225.0)
        val australiaBounds = LatLngBounds(
          targetSouthWest,  // SW bounds
          targetNorthEast // NE bounds
        )
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(australiaBounds, 0))
      }
    }
    setIntentCustomization()
  }

  private fun bindViews() {
    markerImage = findViewById(R.id.marker_center_circle_image_view)
    placeSelectedFab = findViewById(R.id.location_chosen_button)
    myLocationFab = findViewById(R.id.initial_location_button)
    sliderValueTextView = findViewById(R.id.text_view_slider_value)
    distanceSlider = findViewById(R.id.slider_distance_picker)
    infoLayout = findViewById(R.id.information_layout)
  }

  private fun getIntentData() {
    latitude = intent.getDoubleExtra(
        Constants.INITIAL_LATITUDE_INTENT,
        Constants.DEFAULT_LATITUDE
    )
    longitude = intent.getDoubleExtra(
        Constants.INITIAL_LONGITUDE_INTENT,
        Constants.DEFAULT_LONGITUDE
    )
    initLatitude = latitude
    initLongitude = longitude

    markerColorRes = intent.getIntExtra(Constants.MARKER_COLOR_RES_INTENT, -1)
    fabColorRes = intent.getIntExtra(Constants.FAB_COLOR_RES_INTENT, -1)
    circleColorRes = intent.getIntExtra(Constants.CIRCLE_COLOR_RES_INTENT,-1)
    primaryTextColorRes = intent.getIntExtra(Constants.PRIMARY_TEXT_COLOR_RES_INTENT, -1)
    bottomViewColorRes = intent.getIntExtra(Constants.BOTTOM_VIEW_COLOR_RES_INTENT, -1)
    mapRawResourceStyleRes = intent.getIntExtra(Constants.MAP_RAW_STYLE_RES_INTENT, -1)
    mapType = intent.getSerializableExtra(Constants.MAP_TYPE_INTENT) as MapType
    googleApiKey = intent.getStringExtra(Constants.GOOGLE_API_KEY)
    hideLocationButton = intent.getBooleanExtra(Constants.HIDE_LOCATION_BUTTON, false)
    sliderThumbTintColorRes = intent.getIntExtra(Constants.SLIDER_THUMB_COLOR_RES_INTENT,-1)
    sliderTrackInactiveTintColorRes = intent.getIntExtra(Constants.SLIDER_TRACK_INACTIVE_COLOR_RES_INTENT,-1)
    sliderTrackActiveTintColorRes = intent.getIntExtra(Constants.SLIDER_TRACK_ACTIVE_COLOR_RES_INTENT,-1)
    circleRadiusKilometer = intent.getDoubleExtra(
        Constants.INITIAL_CIRCLE_RADIUS_INTENT,
        Constants.DEFAULT_CIRCLE_RADIUS_INTENT
    )
    initCircleRadiusKilometer = circleRadiusKilometer
    sliderValueFrom = intent.getFloatExtra(
        Constants.INITIAL_SLIDER_VALUE_FROM_INTENT,
        Constants.DEFAULT_SLIDER_VALUE_FROM_INTENT
    )
    sliderValueTo = intent.getFloatExtra(
        Constants.INITIAL_SLIDER_VALUE_TO_INTENT,
        Constants.DEFAULT_SLIDER_VALUE_TO_INTENT
    )
  }

  private fun setIntentCustomization() {
    val states = arrayOf(
      intArrayOf(android.R.attr.state_enabled),
      intArrayOf(-android.R.attr.state_enabled),
      intArrayOf(-android.R.attr.state_checked),
      intArrayOf(android.R.attr.state_pressed)
    )
    if (markerColorRes != -1) {
      markerImage.setColorFilter(ContextCompat.getColor(this, markerColorRes))
    }
    if (fabColorRes != -1) {
      placeSelectedFab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, fabColorRes))
      myLocationFab.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(this, fabColorRes))
    }
    if (primaryTextColorRes != -1) {
      sliderValueTextView.setTextColor(ContextCompat.getColor(this, primaryTextColorRes))
    }
    if (bottomViewColorRes !=-1) {
        infoLayout.setBackgroundColor(ContextCompat.getColor(this, bottomViewColorRes))
    }
    myLocationFab.visibility = if(hideLocationButton) View.INVISIBLE else View.VISIBLE
    if (sliderThumbTintColorRes !=-1) {
      val colors = intArrayOf(
        ContextCompat.getColor(this, sliderThumbTintColorRes),
        ContextCompat.getColor(this, sliderThumbTintColorRes),
        ContextCompat.getColor(this, sliderThumbTintColorRes),
        ContextCompat.getColor(this, sliderThumbTintColorRes)
      )
      distanceSlider.thumbTintList=ColorStateList(states,colors)
    }
    if (sliderTrackInactiveTintColorRes !=-1) {
      val colors = intArrayOf(
        ContextCompat.getColor(this, sliderTrackInactiveTintColorRes),
        ContextCompat.getColor(this, sliderTrackInactiveTintColorRes),
        ContextCompat.getColor(this, sliderTrackInactiveTintColorRes),
        ContextCompat.getColor(this, sliderTrackInactiveTintColorRes)
      )
      distanceSlider.trackInactiveTintList=ColorStateList(states,colors)
    }
    if (sliderTrackActiveTintColorRes !=-1) {
      val colors = intArrayOf(
        ContextCompat.getColor(this, sliderTrackActiveTintColorRes),
        ContextCompat.getColor(this, sliderTrackActiveTintColorRes),
        ContextCompat.getColor(this, sliderTrackActiveTintColorRes),
        ContextCompat.getColor(this, sliderTrackActiveTintColorRes)
      )
      distanceSlider.trackActiveTintList=ColorStateList(states,colors)
    }
  }

  private fun setIntentCircleCustomization(googleMap: GoogleMap):Circle {
    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), getZoomLevel(initCircleRadiusKilometer*1000).toFloat()))
    val circleOptions: CircleOptions = CircleOptions()
      .center(googleMap.cameraPosition.target) //set center
      .radius(initCircleRadiusKilometer*1000) //set radius in meters
      .strokeWidth(7.0f)

    if (circleColorRes != -1) {
      circleOptions.fillColor(ContextCompat.getColor(this, circleColorRes))
    }
    if(markerColorRes != -1){
      circleOptions.strokeColor(ContextCompat.getColor(this, markerColorRes))
    }
    return googleMap.addCircle(circleOptions)
  }

  @SuppressLint("SetTextI18n")
  private fun setSliderValueTextView(value:Float){
    roundFloatValue(value,2)
    sliderValueTextView.text="${roundFloatValue(value,2)} km"
  }

  private fun roundFloatValue(value:Float,scale:Int):Double{
    return BigDecimal(value.toDouble()).setScale(scale, RoundingMode.HALF_EVEN).toDouble()
  }

  private fun initSlider(){
    distanceSlider.value=circleRadiusKilometer.toFloat()
    setSliderValueTextView(distanceSlider.value)
    distanceSlider.valueTo=sliderValueTo
    distanceSlider.valueFrom=sliderValueFrom
  }

  override fun onMapReady(googleMap: GoogleMap) {
    map = googleMap
    val circle=setIntentCircleCustomization(googleMap)

    initSlider()

    distanceSlider.addOnChangeListener { rangeSlider, value, fromUser ->
      circleRadiusKilometer=value.toDouble()
      circle.radius=value.toDouble()*1000
      setSliderValueTextView(value)

      val targetNorthEast: LatLng =
        SphericalUtil.computeOffset(circle.center, circle.radius * Math.sqrt(2.0), 45.0)
      val targetSouthWest: LatLng =
        SphericalUtil.computeOffset(circle.center, circle.radius * Math.sqrt(2.0), 225.0)
      val australiaBounds = LatLngBounds(
        targetSouthWest,  // SW bounds
        targetNorthEast // NE bounds
      )
      map.animateCamera(CameraUpdateFactory.newLatLngBounds(australiaBounds, 0))
    }

    map.setOnCameraMoveListener {
      circle.center=map.cameraPosition.target
    }

    map.setOnCameraIdleListener {
      val latLng = map.cameraPosition.target
      latitude = latLng.latitude
      longitude = latLng.longitude
    }

    if (mapRawResourceStyleRes != -1) {
      map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, mapRawResourceStyleRes))
    }

    map.mapType = when(mapType) {
      MapType.NORMAL -> GoogleMap.MAP_TYPE_NORMAL
      MapType.SATELLITE -> GoogleMap.MAP_TYPE_SATELLITE
      MapType.HYBRID -> GoogleMap.MAP_TYPE_HYBRID
      MapType.TERRAIN -> GoogleMap.MAP_TYPE_TERRAIN
      MapType.NONE -> GoogleMap.MAP_TYPE_NONE
      else -> GoogleMap.MAP_TYPE_NORMAL
    }
  }

  private fun getZoomLevel(circleRadius:Double): Double {
    var zoomLevel = 9.0
    val radius = circleRadius + circleRadius / 2
    val scale = radius / 500
    zoomLevel = (16 - ln(scale) / ln(2.0))
    return zoomLevel
  }
}
