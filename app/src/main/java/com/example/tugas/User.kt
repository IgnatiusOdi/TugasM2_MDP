package com.example.tugas
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (
    val name: String,
    val username: String,
    val password: String,
    var wheat: Int = 0,
    var farm: Array<Array<Farm>> =
        Array(4) { i -> Array(4) { j -> Farm("($i,$j)")} },
    var gold: Int = 0,
    var day: Int = 1,
) : Parcelable