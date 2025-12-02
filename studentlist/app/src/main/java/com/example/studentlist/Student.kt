
package com.example.studentlist

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Student(
    val id: String,
    var name: String,
    var phone: String,
    var address: String
) : Parcelable