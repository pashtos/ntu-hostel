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
import com.prilki.ntuhostel.data.toast
import com.prilki.ntuhostel.databinding.FragmentFloorDetailsBinding
import com.prilki.ntuhostel.domain.Repository
import com.prilki.ntuhostel.domain.entities.FloorEntity
import com.prilki.ntuhostel.presentation.adapters.RoomAdapter
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class FloorDetailsFragment : Fragment() {
    private lateinit var binding: FragmentFloorDetailsBinding

    private val repository by lazy { Repository.getInstance(requireActivity().application) }

    private val floor by lazy {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(ENTITY_KEY) ?: throw RuntimeException("floor entity is null")
        } else {
            arguments?.getParcelable(ENTITY_KEY, FloorEntity::class.java)
                ?: throw RuntimeException("floor entity is null")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFloorDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            initInfo()
            setUpDeleteButton()
        }
    }

    private suspend fun initInfo() {
        with(binding) {
            textViewNumber.text = floor.number.toString()
            initRecyclerView()
        }
    }

    private suspend fun initRecyclerView() {
        val roomAdapter = RoomAdapter() {
            val fragment = RoomDetailsFragment.getInstance(it)
            parentFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.fragmentHolder, fragment)
                .commit()
        }
        val rooms = repository.getRooms().filter { it.floor.id == floor.id }
        roomAdapter.submitList(rooms)
        binding.recyclerViewRoomsList.adapter = roomAdapter
        binding.recyclerViewRoomsList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun setUpDeleteButton() {
        binding.buttonDeleteFloor.setOnClickListener {
            lifecycleScope.launch {
                if (repository.getRooms().any { it.floor.id == floor.id }) {
                    toast(requireContext(), "Deleting the floor is impossible until there are related rooms", FancyToast.ERROR)
                }
                else {
                    repository.removeFloor(floor)
                    toast(requireContext(), "Deleted successfully", FancyToast.SUCCESS)
                    delay(1000)
                    parentFragmentManager.popBackStack()
                }
            }
        }
    }

    companion object {
        private const val ENTITY_KEY = "entity_key4"
        fun getInstance(floor: FloorEntity) = FloorDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ENTITY_KEY, floor)
            }
        }
    }
}