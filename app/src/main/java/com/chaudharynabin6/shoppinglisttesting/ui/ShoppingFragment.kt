package com.chaudharynabin6.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.databinding.FragmentShoppingBinding
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment : Fragment(R.layout.fragment_shopping) {
    private lateinit var shoppingViewModel: ShoppingViewModel

    lateinit var binding: FragmentShoppingBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shoppingViewModel = ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
//        testing viewModel working or not
        shoppingViewModel.insertShoppingItem("nabin","21","22")
        binding.apply {
            fabAddShoppingItem.setOnClickListener {
                findNavController().navigate(
                    ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItem()
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }
}