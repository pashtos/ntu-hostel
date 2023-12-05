package com.prilki.ntuhostel.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FloorEntity(
    val id:Int,
    val number: Int,
) : Parcelable
