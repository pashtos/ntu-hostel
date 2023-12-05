package com.prilki.ntuhostel.presentation.views

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.prilki.ntuhostel.R
import com.prilki.ntuhostel.data.toast
import com.prilki.ntuhostel.databinding.FragmentEditRoomBinding
import com.prilki.ntuhostel.domain.Repository
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.launch


class EditRoomFragment : Fragment() {
    private lateinit var binding: FragmentEditRoomBinding

    private val repository by lazy { Repository.getInstance(requireActivity().application) }

    private val room by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ENTITY_KEY)
        } else {
            arguments?.getParcelable(ENTITY_KEY, RoomEntity::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRoomBinding.inflate(inflater, container, false)
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
        room?.let {
            binding.editTextRoomName.setText(it.name)
            binding.editTextCapacity.setText(it.capacity.toString())
        }
        setUpDropdownMenu()
    }

    private suspend fun setUpDropdownMenu() {
        val availableFloors = repository.getFloors().map { it.number }
        val arrayAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, availableFloors)
        binding.autocompleteTextFloor.setAdapter(arrayAdapter)
    }

    private fun setUpSaveButton() {
        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                val name = binding.editTextRoomName.text.toString()
                val capacity = binding.editTextCapacity.text.toString()
                val floorNumber = binding.autocompleteTextFloor.text.toString()
                if (checkName(name) && checkCapacity(capacity) && checkFloor(floorNumber)) {
                    val floorEntity = repository.getFloors().first { it.number == floorNumber.toInt() }
                    if (repository.addRoom(RoomEntity(0, name, capacity.toInt(), floorEntity))) {
                        toast(requireContext(), "Added successfully", FancyToast.SUCCESS)
                    } else {
                        toast(requireContext(), "Room already exists", FancyToast.ERROR)
                    }
                }
            }
        }
    }

    private fun checkName(name: String?): Boolean {
        if (name.isNullOrEmpty()) {
            toast(requireContext(), "No name provided", FancyToast.ERROR)
            return false
        }

        return true
    }

    private fun checkCapacity(capacity: String?): Boolean {
        if (capacity.isNullOrEmpty()) {
            toast(requireContext(), "No capacity provided", FancyToast.ERROR)
            return false
        }
        if (capacity.toInt() < 1 || capacity.toInt() > 10) {
            toast(requireContext(), "Room capacity must be between 1 and 10", FancyToast.ERROR)
            return false
        }
            return true
    }

    private suspend fun checkFloor(number: String?): Boolean {
        if (number.isNullOrEmpty()) {
            toast(requireContext(), "Choose floor", FancyToast.ERROR)
            return false
        }
        return if (repository.getFloors().filter { it.number == number.toInt() }.size == 1) {
            true
        } else {
            toast(requireContext(), "Unknown floor chosen", FancyToast.ERROR)
            false
        }
    }


    companion object {
        private const val ENTITY_KEY = "entity_key1"
        fun getInstance(room: RoomEntity) = EditRoomFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ENTITY_KEY, room)
            }
        }
    }
}