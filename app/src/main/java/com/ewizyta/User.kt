package com.ewizyta;

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(var name : String ?= null, var lastName : String ?= null, var email : String ?= null,
                var password : String ?= null, var phone : String ?= null, var birthDate : String ?= null,
                var gender : String ?= null) : Parcelable
