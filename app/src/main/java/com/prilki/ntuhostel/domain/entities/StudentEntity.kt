package com.prilki.ntuhostel.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StudentEntity(
    val id: Int,
    val name: String,
    val group: String,
    val room: RoomEntity
) : Parcelable
