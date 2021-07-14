package com.map.locationpicker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CircleData(
  var latitude: Double,
  var longitude: Double,
  var radius : Double
): Parcelable {
  override fun toString(): String {
    return latitude.toString()+"\n" +
            longitude.toString()+"\n"+
            radius.toString()+"\n"
  }
}