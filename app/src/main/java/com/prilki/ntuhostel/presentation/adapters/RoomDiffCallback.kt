package com.prilki.ntuhostel.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.prilki.ntuhostel.domain.entities.StudentEntity

class RoomDiffCallback : DiffUtil.ItemCallback<RoomEntity>() {
    override fun areItemsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RoomEntity, newItem: RoomEntity): Boolean {
        return oldItem == newItem
    }
}