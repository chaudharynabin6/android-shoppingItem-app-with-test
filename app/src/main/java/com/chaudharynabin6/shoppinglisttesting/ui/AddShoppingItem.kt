package com.chaudharynabin6.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.databinding.FragmentAddShoppingItemBinding
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddShoppingItem : Fragment(R.layout.fragment_add_shopping_item) {

    lateinit var binding: FragmentAddShoppingItemBinding


    lateinit var shoppingViewModel: ShoppingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingViewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        shoppingViewModel.curImageUrl.value
        binding.apply {
            ivShoppingImage.setOnClickListener {
                findNavController().navigate(
                    AddShoppingItemDirections.actionAddShoppingItemToImagePickFragment()
                )
            }
        }
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                shoppingViewModel.setCurImageUrl("")
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


}