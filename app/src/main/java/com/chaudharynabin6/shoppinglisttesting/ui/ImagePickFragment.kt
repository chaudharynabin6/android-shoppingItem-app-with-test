package com.chaudharynabin6.shoppinglisttesting.ui

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.adapter.ImageAdapter
import com.chaudharynabin6.shoppinglisttesting.databinding.FragmentImagePickBinding
import com.chaudharynabin6.shoppinglisttesting.other.Constants
import com.chaudharynabin6.shoppinglisttesting.other.Status
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImagePickFragment @Inject constructor(
    val imageAdapter: ImageAdapter,
) : Fragment(R.layout.fragment_image_pick) {

    lateinit var shoppingViewModel: ShoppingViewModel

    lateinit var binding: FragmentImagePickBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingViewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]

        setupRecyclerView()
        eventsHandling()
        subscribeToObservers()
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

        var job: Job? = null
        binding.etSearch.addTextChangedListener { text: Editable? ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(Constants.SEARCH_TIME_DELAY)
                text?.let {
                    if (text.toString().isNotEmpty()) {
                        shoppingViewModel.searchImage(text.toString())
                    }
                }
            }
        }
    }

    private fun subscribeToObservers() {
        shoppingViewModel.images.observe(viewLifecycleOwner) {
            it?.getContentIfNotHandled()?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        val urls = resource.data?.hits?.map { imageResult ->
                            imageResult.previewURL
                        }
                        imageAdapter.images = urls ?: listOf()
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        Snackbar.make(
                            requireView(),
                            resource.message ?: "An unknown error occured",
                            Snackbar.LENGTH_LONG
                        ).apply {
                            show()
                        }
                        binding.progressBar.visibility = View.GONE
                    }
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                }

            }

        }
    }
}