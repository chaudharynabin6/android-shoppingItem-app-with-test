package com.chaudharynabin6.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.databinding.FragmentAddShoppingItemBinding
import com.chaudharynabin6.shoppinglisttesting.other.Status
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddShoppingItem @Inject constructor(
    private val glide: RequestManager,
    var shoppingViewModel: ShoppingViewModel? = null,
) : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var binding: FragmentAddShoppingItemBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingViewModel =
            shoppingViewModel ?: ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        binding.apply {
            ivShoppingImage.setOnClickListener {
                findNavController().navigate(
                    AddShoppingItemDirections.actionAddShoppingItemToImagePickFragment()
                )
            }

            btnAddShoppingItem.setOnClickListener {
                shoppingViewModel?.insertShoppingItem(
                    etShoppingItemName.text.toString(),
                    etShoppingItemAmount.text.toString(),
                    etShoppingItemPrice.text.toString()
                )
            }
            subscribeLiveData()
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                shoppingViewModel?.setCurImageUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddShoppingItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun subscribeLiveData() {
        binding.apply {
            shoppingViewModel?.curImageUrl?.observe(viewLifecycleOwner) {
                glide.load(it).into(ivShoppingImage)
            }

            shoppingViewModel?.insertShoppingItemStatus?.observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            Snackbar.make(
                                requireView(),
                                "Shopping Item Added",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        Status.ERROR -> {
                            Snackbar.make(
                                requireView(),
                                resource.message ?: "An unknown error occurred",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        Status.LOADING -> {
                            /* NO-OP */
                        }
                    }
                }
            }
        }


    }

}