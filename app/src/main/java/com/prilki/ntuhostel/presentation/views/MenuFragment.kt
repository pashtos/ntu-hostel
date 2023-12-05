package com.prilki.ntuhostel.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.prilki.ntuhostel.R
import com.prilki.ntuhostel.data.ENTITY_FLOOR
import com.prilki.ntuhostel.data.ENTITY_ROOM
import com.prilki.ntuhostel.data.ENTITY_STUDENT
import com.prilki.ntuhostel.databinding.FragmentMenuBinding
import com.prilki.ntuhostel.domain.Repository
import com.prilki.ntuhostel.domain.entities.FloorEntity
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.prilki.ntuhostel.domain.entities.StudentEntity
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding

    private val repository by lazy { Repository.getInstance(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOnClickListeners()
        addEntities()
    }

    private fun addEntities() {
        lifecycleScope.launch {
            repository.addFloor(FloorEntity(0, 1))
            repository.addFloor(FloorEntity(0, 2))
            repository.addFloor(FloorEntity(0, 3))
            repository.addRoom(RoomEntity(0, "316", 3, repository.getFloorById(3)))
            repository.addRoom(RoomEntity(0, "321", 5, repository.getFloorById(3)))
            repository.addRoom(RoomEntity(0, "317", 5, repository.getFloorById(3)))
            repository.addRoom(RoomEntity(0, "222", 3, repository.getFloorById(2)))
            repository.addRoom(RoomEntity(0, "221", 5, repository.getFloorById(2)))
            repository.addRoom(RoomEntity(0, "219", 5, repository.getFloorById(2)))
            repository.addRoom(RoomEntity(0, "119", 3, repository.getFloorById(1)))
            repository.addRoom(RoomEntity(0, "100", 5, repository.getFloorById(1)))
            repository.addRoom(RoomEntity(0, "131", 5, repository.getFloorById(1)))
            repository.addStudent(StudentEntity(0, "Dmytro", "PRm-1-5", repository.getRoomById(1)))
            repository.addStudent(StudentEntity(0, "Stepan", "PRm-1-7", repository.getRoomById(1)))
            repository.addStudent(StudentEntity(0, "Sophia", "PRm-1-6", repository.getRoomById(2)))
            repository.addStudent(StudentEntity(0, "Pavlo", "PRm-1-5", repository.getRoomById(2)))
            repository.addStudent(StudentEntity(0, "Valentin", "PRm-1-1", repository.getRoomById(3)))
            repository.addStudent(StudentEntity(0, "Vlad", "PRm-1-1", repository.getRoomById(3)))
            repository.addStudent(StudentEntity(0, "Victoria", "PRm-1-2", repository.getRoomById(7)))
            repository.addStudent(StudentEntity(0, "Natali", "PRm-1-3", repository.getRoomById(8)))
            repository.addStudent(StudentEntity(0, "Bohdan", "PRm-1-4", repository.getRoomById(9)))
            repository.addStudent(StudentEntity(0, "Maxim", "PR-1-5", repository.getRoomById(4)))
            repository.addStudent(StudentEntity(0, "Volodymyr", "PR-1-7", repository.getRoomById(4)))
            repository.addStudent(StudentEntity(0, "Lev", "PR-1-6", repository.getRoomById(4)))
            repository.addStudent(StudentEntity(0, "Evgeniy", "PR-1-5", repository.getRoomById(5)))
            repository.addStudent(StudentEntity(0, "Anastasia", "PR-1-1", repository.getRoomById(5)))
            repository.addStudent(StudentEntity(0, "Denis", "PR-1-1", repository.getRoomById(6)))
            repository.addStudent(StudentEntity(0, "Olexander", "PR-1-2", repository.getRoomById(6)))
            repository.addStudent(StudentEntity(0, "Daryna", "PR-1-3", repository.getRoomById(7)))
            repository.addStudent(StudentEntity(0, "Ruslan", "PR-1-4", repository.getRoomById(8)))
        }
    }

    private fun setOnClickListeners() {
        binding.buttonGoToFloors.setOnClickListener {
            launchEntitiesList(ENTITY_FLOOR)
        }
        binding.buttonGoToRooms.setOnClickListener {
            launchEntitiesList(ENTITY_ROOM)
        }
        binding.buttonGoToStudents.setOnClickListener {
            launchEntitiesList(ENTITY_STUDENT)
        }
    }

    private fun launchEntitiesList(entity: Int) {
        val fragment = EntitiesListFragment.getInstance(entity)
        parentFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.fragmentHolder, fragment).commit()
    }

}