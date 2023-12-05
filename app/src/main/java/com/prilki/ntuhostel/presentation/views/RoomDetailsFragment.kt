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
import com.prilki.ntuhostel.databinding.FragmentRoomDetailsBinding
import com.prilki.ntuhostel.domain.Repository
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.prilki.ntuhostel.presentation.adapters.StudentAdapter
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RoomDetailsFragment : Fragment() {
    private lateinit var binding: FragmentRoomDetailsBinding

    private val repository by lazy { Repository.getInstance(requireActivity().application) }

    private val room by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ENTITY_KEY) ?: throw RuntimeException("room entity is null")
        } else {
            arguments?.getParcelable(ENTITY_KEY, RoomEntity::class.java)
                ?: throw RuntimeException("room entity is null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRoomDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initInfo()
        }
    }

    private suspend fun initInfo() {
        val availableSpots =
            room.capacity - repository.getStudents().filter { it.room.id == room.id }.size
        with(binding) {
            textViewName.text = room.name
            textViewFloor.text = room.floor.number.toString()
            textViewStudentsLimit.text = room.capacity.toString()
            textViewStudentsAvailable.text = availableSpots.toString()
            initRecyclerView()
            setOnClickListeners()
            if (!ADMIN_MODE) {
                binding.buttonDeleteRoom.visibility = View.GONE
            }
        }
    }

    private suspend fun initRecyclerView() {
        val studentAdapter = StudentAdapter() {
            val fragment = StudentDetailsFragment.getInstance(it)
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentHolder, fragment)
                .commit()
        }
        val students = repository.getStudents().filter { it.room.id == room.id }
        studentAdapter.submitList(students)
        binding.recyclerViewResidentsList.adapter = studentAdapter
        binding.recyclerViewResidentsList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setOnClickListeners() {
        binding.textViewFloor.setOnClickListener {
            val fragment = FloorDetailsFragment.getInstance(room.floor)
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentHolder, fragment)
                .commit()
        }
        binding.buttonDeleteRoom.setOnClickListener {
            lifecycleScope.launch {
                if (repository.getStudents().any { it.room.id == room.id }) {
                    toast(
                        requireContext(),
                        "Deleting the room is impossible until there are related rooms",
                        FancyToast.ERROR
                    )
                } else {
                    repository.removeRoom(room)
                    toast(requireContext(), "Deleted successfully", FancyToast.SUCCESS)
                    delay(1000)
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }


    companion object {
        private const val ENTITY_KEY = "entity_key5"
        fun getInstance(room: RoomEntity) = RoomDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ENTITY_KEY, room)
            }
        }
    }
}