package com.example.tugas
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Farm (
    val location: String,
    var planted: Boolean = false,
    val harvestTime: Int = 3,
    var tillHarvestTime: Int = 3,
    var expiredTime: Int = 0,
) : Parcelable