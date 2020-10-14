package com.example.netservice

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class User(val description: String?, val lowQ: Bitmap?, val highQ: String?)
//    : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readParcelable(Bitmap::class.java.classLoader),
//        parcel.readString()
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(description)
//        parcel.writeParcelable(lowQ, flags)
//        parcel.writeString(highQ)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<User> {
//        override fun createFromParcel(parcel: Parcel): User {
//            return User(parcel)
//        }
//
//        override fun newArray(size: Int): Array<User?> {
//            return arrayOfNulls(size)
//        }
//    }
//}