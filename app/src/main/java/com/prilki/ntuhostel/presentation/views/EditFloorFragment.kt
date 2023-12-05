package com.prilki.ntuhostel.presentation.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.prilki.ntuhostel.data.toast
import com.prilki.ntuhostel.databinding.FragmentEditFloorBinding
import com.prilki.ntuhostel.domain.Repository
import com.prilki.ntuhostel.domain.entities.FloorEntity
import com.prilki.ntuhostel.domain.entities.RoomEntity
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class EditFloorFragment : Fragment() {
    private lateinit var binding: FragmentEditFloorBinding

    private val repository by lazy { Repository.getInstance(requireActivity().application) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditFloorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            setUpSaveButton()
        }
    }


    private fun setUpSaveButton() {
        binding.buttonSave.setOnClickListener {
            lifecycleScope.launch {
                val number = binding.editTextNumber.text.toString()
                if (checkNumber(number)) {
                    if (repository.addFloor(FloorEntity(0, number.toInt()))) {
                        toast(requireContext(), "The floor successfully added", FancyToast.SUCCESS)
                        delay(1000)
                        parentFragmentManager.popBackStack()
                    }
                    else {
                        toast(requireContext(), "Error adding floor", FancyToast.ERROR)
                    }
                }
            }
        }
    }

    private suspend fun checkNumber(number: String?): Boolean {
        if (number.isNullOrEmpty()) {
            toast(requireContext(), "No number provided", FancyToast.ERROR)
            return false
        }
        if (number.toInt() < 1) {
            toast(requireContext(), "Floor number must be higher than 0", FancyToast.ERROR)
            return false
        }
        if (repository.getFloors().any { it.number == number.toInt() }) {
            toast(requireContext(), "Such a floor already exists", FancyToast.ERROR)
            return false
        }
        return true
    }


    companion object {
        private const val ENTITY_KEY = "entity_key1"
        fun getInstance(room: RoomEntity) = EditFloorFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ENTITY_KEY, room)
            }
        }
    }
}