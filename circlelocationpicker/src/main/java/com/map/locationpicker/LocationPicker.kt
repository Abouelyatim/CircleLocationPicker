package com.map.locationpicker

import android.app.Activity
import android.content.Intent
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import com.map.locationpicker.Constants.CIRCLE_COLOR_RES_INTENT

class LocationPicker {

  class IntentBuilder {

    private lateinit var activity: Activity
    private var latitude: Double = Constants.DEFAULT_LATITUDE
    private var longitude: Double = Constants.DEFAULT_LONGITUDE
    private var markerImageColorRes: Int = -1
    private var circleBackgroundColorRes: Int = -1
    private var fabBackgroundColorRes: Int = -1
    private var primaryTextColorRes: Int = -1
    private var bottomViewColorRes:  Int = -1
    private var mapRawResourceStyleRes: Int = -1
    private var confirmButtonBackgroundRes: Int = -1
    private var confirmButtonTextColorRes: Int = -1
    private var confirmButtonTextRes: String = Constants.DEFAULT_CONFIRM_TEXT
    private var mapType: MapType = MapType.NORMAL
    private var googleApiKey: String? = null
    private var hideLocation:Boolean =false
    private var sliderThumbTintColorRes: Int = -1
    private var sliderTrackInactiveTintColorRes: Int = -1
    private var sliderTrackActiveTintColorRes: Int = -1
    private var circleRadiusKilometer:Double= Constants.DEFAULT_CIRCLE_RADIUS_INTENT
    private var sliderValueFrom:Float= Constants.DEFAULT_SLIDER_VALUE_FROM_INTENT
    private var sliderValueTo:Float= Constants.DEFAULT_SLIDER_VALUE_TO_INTENT

    fun setConfirmButtonBackgroundShape(@DrawableRes confirmButtonBackgroundRes: Int) = apply { this.confirmButtonBackgroundRes = confirmButtonBackgroundRes }

    fun setConfirmButtonTextColor(@ColorRes confirmButtonTextColorRes: Int) = apply { this.confirmButtonTextColorRes = confirmButtonTextColorRes }

    fun setConfirmButtonText(confirmButtonTextRes: String) = apply { this.confirmButtonTextRes = confirmButtonTextRes }

    fun hideLocationButton(hideLocation: Boolean) = apply { this.hideLocation = hideLocation }

    fun setSliderThumbTintColor(@ColorRes sliderThumbTintColorRes: Int) = apply { this.sliderThumbTintColorRes = sliderThumbTintColorRes }

    fun setSliderTrackInactiveTintColor(@ColorRes sliderTrackInactiveTintColorRes: Int) = apply { this.sliderTrackInactiveTintColorRes = sliderTrackInactiveTintColorRes }

    fun setSliderTrackActiveTintColor(@ColorRes sliderTrackActiveTintColorRes: Int) = apply { this.sliderTrackActiveTintColorRes = sliderTrackActiveTintColorRes }

    fun setInitialCircleRadiusKilometer(radius: Double) = apply { this.circleRadiusKilometer = radius }

    fun setSliderValueFrom(from: Float) = apply { this.sliderValueFrom = from }

    fun setSliderValueTo(to: Float) = apply { this.sliderValueTo = to }

    fun setLatLong(
      latitude: Double,
      longitude: Double
    ) = apply {
      if (latitude == -1.0 || longitude == -1.0) {
        this.latitude = Constants.DEFAULT_LATITUDE
        this.longitude = Constants.DEFAULT_LONGITUDE
      } else {
        this.latitude = latitude
        this.longitude = longitude
      }
    }

    fun setPlaceSearchBar(googleApiKey: String? = null) = apply {
      this.googleApiKey = googleApiKey
    }

    fun setMarkerImageImageColor(@ColorRes markerImageColorRes: Int) = apply { this.markerImageColorRes = markerImageColorRes }

    fun setFabColor(@ColorRes fabBackgroundColor: Int) = apply { this.fabBackgroundColorRes = fabBackgroundColor }

    fun setPrimaryTextColor(@ColorRes primaryTextColor: Int) = apply { this.primaryTextColorRes = primaryTextColor }

    fun setCircleBackgroundColorRes(@ColorRes circleBackgroundColorRes:Int) = apply { this.circleBackgroundColorRes = circleBackgroundColorRes }

    fun setBottomViewColor(@ColorRes bottomViewColor: Int) = apply { this.bottomViewColorRes = bottomViewColor }

    fun setMapRawResourceStyle(@RawRes mapRawResourceStyleRes: Int) = apply { this.mapRawResourceStyleRes = mapRawResourceStyleRes }

    fun setMapType(mapType: MapType) = apply { this.mapType = mapType }

    fun build(activity: Activity): Intent {
      this.activity = activity
      val intent = Intent(activity, LocationPickerActivity::class.java)
      intent.putExtra(Constants.INITIAL_LATITUDE_INTENT, latitude)
      intent.putExtra(Constants.INITIAL_LONGITUDE_INTENT, longitude)
      intent.putExtra(Constants.MARKER_COLOR_RES_INTENT, markerImageColorRes)
      intent.putExtra(CIRCLE_COLOR_RES_INTENT,circleBackgroundColorRes)
      intent.putExtra(Constants.FAB_COLOR_RES_INTENT, fabBackgroundColorRes)
      intent.putExtra(Constants.PRIMARY_TEXT_COLOR_RES_INTENT, primaryTextColorRes)
      intent.putExtra(Constants.BOTTOM_VIEW_COLOR_RES_INTENT, bottomViewColorRes)
      intent.putExtra(Constants.MAP_RAW_STYLE_RES_INTENT, mapRawResourceStyleRes)
      intent.putExtra(Constants.MAP_TYPE_INTENT, mapType)
      intent.putExtra(Constants.GOOGLE_API_KEY, googleApiKey)
      intent.putExtra(Constants.HIDE_LOCATION_BUTTON, hideLocation)
      intent.putExtra(Constants.SLIDER_THUMB_COLOR_RES_INTENT, sliderThumbTintColorRes)
      intent.putExtra(Constants.SLIDER_TRACK_ACTIVE_COLOR_RES_INTENT, sliderTrackActiveTintColorRes)
      intent.putExtra(Constants.SLIDER_TRACK_INACTIVE_COLOR_RES_INTENT, sliderTrackInactiveTintColorRes)
      intent.putExtra(Constants.INITIAL_SLIDER_VALUE_FROM_INTENT, sliderValueFrom)
      intent.putExtra(Constants.INITIAL_SLIDER_VALUE_TO_INTENT, sliderValueTo)
      intent.putExtra(Constants.INITIAL_CIRCLE_RADIUS_INTENT, circleRadiusKilometer)
      intent.putExtra(Constants.CONFIRM_BUTTON_BACKGROUND_RES_INTENT, confirmButtonBackgroundRes)
      intent.putExtra(Constants.CONFIRM_BUTTON_TEXT_COLOR_RES_INTENT, confirmButtonTextColorRes)
      intent.putExtra(Constants.CONFIRM_BUTTON_TEXT_RES_INTENT, confirmButtonTextRes)
      return intent
    }
  }
}
