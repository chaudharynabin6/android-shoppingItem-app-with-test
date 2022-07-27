package com.chaudharynabin6.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.adapter.ImageAdapter
import com.chaudharynabin6.shoppinglisttesting.databinding.FragmentImagePickBinding
import com.chaudharynabin6.shoppinglisttesting.other.Constants
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import javax.inject.Inject

class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter,
) : Fragment(R.layout.fragment_image_pick) {

    lateinit var shoppingViewModel: ShoppingViewModel

    lateinit var binding: FragmentImagePickBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        shoppingViewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]

        eventsHandling()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentImagePickBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.apply {

            rvImages.apply {
                adapter = imageAdapter
                layoutManager = GridLayoutManager(requireContext(), Constants.GRID_SPAN_COUNT)
            }
        }
    }

    private fun eventsHandling() {
        imageAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            shoppingViewModel.setCurImageUrl(it)
        }
    }
}