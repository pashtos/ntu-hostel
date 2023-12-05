package com.prilki.ntuhostel.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.prilki.ntuhostel.domain.entities.FloorEntity

class FloorDiffCallback : DiffUtil.ItemCallback<FloorEntity>() {
    override fun areItemsTheSame(oldItem: FloorEntity, newItem: FloorEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: FloorEntity, newItem: FloorEntity): Boolean {
        return oldItem == newItem
    }
}