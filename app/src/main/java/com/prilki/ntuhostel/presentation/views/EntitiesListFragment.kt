package com.prilki.ntuhostel.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.prilki.ntuhostel.R
import com.prilki.ntuhostel.data.ADMIN_MODE
import com.prilki.ntuhostel.data.ENTITY_FLOOR
import com.prilki.ntuhostel.data.ENTITY_ROOM
import com.prilki.ntuhostel.data.ENTITY_STUDENT
import com.prilki.ntuhostel.databinding.FragmentEntitiesListBinding
import com.prilki.ntuhostel.domain.Repository
import com.prilki.ntuhostel.domain.entities.FloorEntity
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.prilki.ntuhostel.domain.entities.StudentEntity
import com.prilki.ntuhostel.presentation.adapters.FloorAdapter
import com.prilki.ntuhostel.presentation.adapters.RoomAdapter
import com.prilki.ntuhostel.presentation.adapters.StudentAdapter
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class EntitiesListFragment : Fragment() {
    private val repository by lazy { Repository.getInstance(requireActivity().application) }
    private var entityType by Delegates.notNull<Int>()
    private lateinit var binding: FragmentEntitiesListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entityType = arguments?.getInt(ENTITY_KEY) ?: throw RuntimeException("entity type is null")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntitiesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            setUpListAdapter()
            setUpAddButton()
        }
    }

    private suspend fun setUpListAdapter() {
        when (entityType) {
            ENTITY_FLOOR -> setUpForFloors()
            ENTITY_STUDENT -> setUpForStudents()
            ENTITY_ROOM -> setUpForRooms()
        }
    }

    private suspend fun setUpForStudents() {
        val students = repository.getStudents()
        students.ifEmpty {
            binding.textViewEmptyListMessage.visibility = View.VISIBLE
            return
        }
        val onItemCLickedCallback: (StudentEntity) -> Unit = { studentEntity ->
            val fragment = StudentDetailsFragment.getInstance(studentEntity)
            launchDetailsFragment(fragment)
        }
        val studentAdapter = StudentAdapter(onItemCLickedCallback)
        studentAdapter.submitList(students)
        binding.recyclerViewEntities.adapter = studentAdapter

    }

    private suspend fun setUpForRooms() {
        val rooms = repository.getRooms()
        rooms.ifEmpty {
            binding.textViewEmptyListMessage.visibility = View.VISIBLE
            return
        }
        val onItemCLickedCallback: (RoomEntity) -> Unit = { roomEntity ->
            val fragment = RoomDetailsFragment.getInstance(roomEntity)
            launchDetailsFragment(fragment)
        }
        val roomAdapter = RoomAdapter(onItemCLickedCallback)
        roomAdapter.submitList(rooms)
        binding.recyclerViewEntities.adapter = roomAdapter
    }

    private suspend fun setUpForFloors() {
        val floors = repository.getFloors()
        floors.ifEmpty {
            binding.textViewEmptyListMessage.visibility = View.VISIBLE
            return
        }
        val onItemCLickedCallback: (FloorEntity) -> Unit = { floorEntity ->
            val fragment = FloorDetailsFragment.getInstance(floorEntity)
            launchDetailsFragment(fragment)
        }
        val floorAdapter = FloorAdapter(onItemCLickedCallback)
        floorAdapter.submitList(floors)
        binding.recyclerViewEntities.adapter = floorAdapter
    }

    private fun launchDetailsFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.fragmentHolder, fragment).commit()
    }

    private fun setUpAddButton() {
        if (ADMIN_MODE) {
            binding.buttonAddItem.visibility = View.VISIBLE
            binding.buttonAddItem.setOnClickListener {
                openEditor()
            }
        }
    }

    private fun openEditor() {
        val fragment = when (entityType) {
            ENTITY_ROOM -> EditRoomFragment()
            ENTITY_STUDENT -> EditStudentFragment()
            ENTITY_FLOOR -> EditFloorFragment()
            else -> throw RuntimeException("unknown entity type")
        }
        parentFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.fragmentHolder, fragment).commit()
    }

    companion object {
        private const val ENTITY_KEY = "entity_key3"
        fun getInstance(entity: Int) = EntitiesListFragment().apply {
            arguments = Bundle().apply {
                putInt(ENTITY_KEY, entity)
            }
        }
    }
}