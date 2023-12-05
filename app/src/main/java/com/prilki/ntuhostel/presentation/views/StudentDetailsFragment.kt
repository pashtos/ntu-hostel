package com.prilki.ntuhostel.presentation.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.prilki.ntuhostel.R
import com.prilki.ntuhostel.data.ADMIN_MODE
import com.prilki.ntuhostel.data.toast
import com.prilki.ntuhostel.databinding.FragmentStudentDetailsBinding
import com.prilki.ntuhostel.domain.Repository
import com.prilki.ntuhostel.domain.entities.StudentEntity
import com.prilki.ntuhostel.presentation.adapters.StudentAdapter
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class StudentDetailsFragment : Fragment() {
    private lateinit var binding: FragmentStudentDetailsBinding

    private val repository by lazy { Repository.getInstance(requireActivity().application) }

    private val student by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ENTITY_KEY) ?: throw RuntimeException("student entity is null")
        } else {
            arguments?.getParcelable(ENTITY_KEY, StudentEntity::class.java)
                ?: throw RuntimeException("student entity is null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initInfo()
            setOnClickListeners()
        }
    }

    private suspend fun initInfo() {
        with(binding) {
            textViewName.text = student.name
            textViewGroup.text = student.group
            textViewRoom.text = student.room.name
            textViewFloor.text = student.room.floor.number.toString()
            initRecyclerView()
            if (!ADMIN_MODE) {
                binding.buttonEditStudent.visibility = View.GONE
                binding.buttonDeleteStudent.visibility = View.GONE
            }
        }
    }

    private suspend fun initRecyclerView() {
        val studentAdapter = StudentAdapter() {
            val fragment = getInstance(it)
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentHolder, fragment)
                .commit()
        }
        val students =
            repository.getStudents().filter { it.room == student.room && it.id != student.id }
        studentAdapter.submitList(students)
        binding.recyclerViewResidentsList.adapter = studentAdapter
        binding.recyclerViewResidentsList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setOnClickListeners() {
        binding.textViewFloor.setOnClickListener {
            val fragment = FloorDetailsFragment.getInstance(student.room.floor)
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentHolder, fragment)
                .commit()
        }
        binding.textViewRoom.setOnClickListener {
            val fragment = RoomDetailsFragment.getInstance(student.room)
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentHolder, fragment)
                .commit()
        }

        binding.buttonDeleteStudent.setOnClickListener {
            lifecycleScope.launch {
                repository.removeStudent(student)
                toast(requireContext(), "Deleted successfully", FancyToast.SUCCESS)
                delay(1000)
                parentFragmentManager.popBackStack()
            }
        }

        binding.buttonEditStudent.setOnClickListener {
            val fragment = EditStudentFragment.getInstance(student)
            parentFragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.fragmentHolder, fragment).commit()
        }
    }


    companion object {
        private const val ENTITY_KEY = "entity_key5"
        fun getInstance(student: StudentEntity) = StudentDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ENTITY_KEY, student)
            }
        }
    }
}