package com.ewizyta

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Doctor(
    val name: String,
    val surname: String,
    val age: Int,
    val specialization: String
) : Parcelable {

}