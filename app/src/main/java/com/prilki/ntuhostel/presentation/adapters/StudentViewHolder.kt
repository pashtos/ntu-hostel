package com.prilki.ntuhostel.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.prilki.ntuhostel.databinding.StudentItemBinding

class StudentViewHolder(binding: StudentItemBinding) : RecyclerView.ViewHolder(binding.root) {
    val name = binding.studentName
    val studentGroup = binding.studentGroup
    val studentRoom = binding.studentRoom
}