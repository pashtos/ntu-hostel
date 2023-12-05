package com.prilki.ntuhostel.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RoomEntity(
    val id: Int,
    val name: String,
    val capacity: Int,
    val floor: FloorEntity
) : Parcelable
