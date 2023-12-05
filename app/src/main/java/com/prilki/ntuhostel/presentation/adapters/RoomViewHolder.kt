package com.prilki.ntuhostel.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.prilki.ntuhostel.databinding.RoomItemBinding
import com.prilki.ntuhostel.databinding.StudentItemBinding

class RoomViewHolder(binding: RoomItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val name = binding.roomName
}