package com.map.locationpicker

import android.location.Address
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CircleData(
  var latitude: Double,
  var longitude: Double,
  var radius : Double,
  var addressList: List<Address>? = null
): Parcelable {
  override fun toString(): String {
    return latitude.toString()+"\n" +
            longitude.toString()+"\n"+
            radius.toString()+"\n"+
            getAddressString()
  }
  private fun getAddressString():String {
    addressList?.let { if(it.isNotEmpty()) return it[0].getAddressLine(0)}
    return ""
  }
}