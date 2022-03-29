package com.beemo.daumimagesearchapp.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime
import java.util.*

@Parcelize
data class KakaoImage (
    @SerializedName("display_sitename")
    val siteName : String,
    val datetime : Date,
    val image_url : String
) : Parcelable