package com.prilki.ntuhostel.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.prilki.ntuhostel.databinding.FloorItemBinding
import com.prilki.ntuhostel.databinding.RoomItemBinding
import com.prilki.ntuhostel.databinding.StudentItemBinding
import com.prilki.ntuhostel.domain.entities.FloorEntity
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.prilki.ntuhostel.domain.entities.StudentEntity

class FloorAdapter(
    private val onItemClickListener: (FloorEntity) -> Unit,
) : ListAdapter<FloorEntity, FloorViewHolder>(FloorDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FloorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FloorItemBinding.inflate(inflater, parent, false)
        return FloorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FloorViewHolder, position: Int) {
        val floor = getItem(position)
        holder.name.text = floor.number.toString()
        holder.itemView.setOnClickListener { onItemClickListener(floor) }
    }
}