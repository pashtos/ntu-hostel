package com.prilki.ntuhostel.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.prilki.ntuhostel.databinding.RoomItemBinding
import com.prilki.ntuhostel.databinding.StudentItemBinding
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.prilki.ntuhostel.domain.entities.StudentEntity

class RoomAdapter(
    private val onItemClickListener: (RoomEntity) -> Unit,
) : ListAdapter<RoomEntity, RoomViewHolder>(RoomDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RoomItemBinding.inflate(inflater, parent, false)
        return RoomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = getItem(position)
        holder.name.text = room.name
        holder.itemView.setOnClickListener { onItemClickListener(room) }
    }
}