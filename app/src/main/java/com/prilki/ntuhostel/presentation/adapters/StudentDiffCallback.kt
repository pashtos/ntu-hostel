package com.prilki.ntuhostel.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.prilki.ntuhostel.domain.entities.StudentEntity

class StudentDiffCallback : DiffUtil.ItemCallback<StudentEntity>() {
    override fun areItemsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: StudentEntity, newItem: StudentEntity): Boolean {
        return oldItem == newItem
    }
}