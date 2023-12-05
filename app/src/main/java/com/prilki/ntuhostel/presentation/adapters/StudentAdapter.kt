package com.prilki.ntuhostel.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.prilki.ntuhostel.databinding.StudentItemBinding
import com.prilki.ntuhostel.domain.entities.StudentEntity

class StudentAdapter(
    private val onItemClickListener: (StudentEntity) -> Unit,
) : ListAdapter<StudentEntity, StudentViewHolder>(StudentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = StudentItemBinding.inflate(inflater, parent, false)
        return StudentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = getItem(position)
        holder.name.text = student.name
        holder.studentGroup.text = student.group
        holder.studentRoom.text = student.room.name
        holder.itemView.setOnClickListener { onItemClickListener(student) }
    }
}