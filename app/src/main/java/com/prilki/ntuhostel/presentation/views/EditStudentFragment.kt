package com.prilki.ntuhostel.presentation.views

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.prilki.ntuhostel.R
import com.prilki.ntuhostel.data.LOG_TAG
import com.prilki.ntuhostel.data.toast
import com.prilki.ntuhostel.databinding.FragmentEditStudentBinding
import com.prilki.ntuhostel.domain.Repository
import com.prilki.ntuhostel.domain.entities.StudentEntity
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class EditStudentFragment : Fragment() {
    private lateinit var binding: FragmentEditStudentBinding

    private val repository by lazy { Repository.getInstance(requireActivity().application) }

    private val student by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ENTITY_KEY)
        } else {
            arguments?.getParcelable(ENTITY_KEY, StudentEntity::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initInfo()
            setUpSaveButton()
        }
    }

    private suspend fun initInfo() {
        student?.let {
            binding.editTextStudentName.setText(it.name)
            binding.editTextGroup.setText(it.group)
        }
        setUpDropdownMenu()
    }

    private suspend fun setUpDropdownMenu() {
        val students = repository.getStudents()
        val availableRooms = repository.getRooms().filter { room ->
            room.capacity - students.filter { it.room.id == room.id }.size > 0 }.map { it.name }
        Log.d(LOG_TAG, availableRooms.toString())
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, availableRooms )
        binding.autocompleteTextRoom.setAdapter(arrayAdapter)
    }

    private fun setUpSaveButton() {
        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                val name = binding.editTextStudentName.text.toString()
                val group = binding.editTextGroup.text.toString()
                val room = binding.autocompleteTextRoom.text.toString()
                if (checkName(name) && checkGroup(group) && checkRoom(room)) {
                    val roomEntity = repository.getRooms().first{ it.name == room }
                    if(repository.addStudent(StudentEntity(0, name, group, roomEntity))) {
                        toast(requireContext(), "Added successfully", FancyToast.SUCCESS)
                        delay(1000)
                        parentFragmentManager.popBackStack()
                    }else {
                        toast(requireContext(), "Student already exists", FancyToast.ERROR)
                    }
                }
            }
        }

    }

    private fun checkName(name: String?): Boolean {
        if(name.isNullOrEmpty()) {
            toast(requireContext(), "No name provided", FancyToast.ERROR)
            return false
        }
        return true
    }

    private fun checkGroup(group: String?): Boolean {
        if(group.isNullOrEmpty()) {
            toast(requireContext(), "No group provided", FancyToast.ERROR)
            return false
        }
        return true
    }

    private suspend fun checkRoom(name: String?): Boolean {
        if(name.isNullOrEmpty()) {
            toast(requireContext(), "Choose room", FancyToast.ERROR)
            return false
        }
        return if(repository.getRooms().filter { it.name == name }.size == 1) {
            true
        } else {
            toast(requireContext(), "Unknown room chosen", FancyToast.ERROR)
            false
        }
    }

    companion object {
        private const val ENTITY_KEY = "entity_key2"
        fun getInstance(student: StudentEntity) = EditStudentFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ENTITY_KEY, student)
            }
        }
    }
}