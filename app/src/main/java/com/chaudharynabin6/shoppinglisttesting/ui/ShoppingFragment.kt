package com.chaudharynabin6.shoppinglisttesting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.adapter.ShoppingItemAdapter
import com.chaudharynabin6.shoppinglisttesting.databinding.FragmentShoppingBinding
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingFragment @Inject constructor(
    private val shoppingItemAdapter: ShoppingItemAdapter,
    var shoppingViewModel: ShoppingViewModel? = null,
) : Fragment(R.layout.fragment_shopping) {

    lateinit var viewModel: ShoppingViewModel
    lateinit var binding: FragmentShoppingBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shoppingViewModel =
            shoppingViewModel ?: ViewModelProvider(requireActivity())[ShoppingViewModel::class.java]
        viewModel = shoppingViewModel!!
        binding.apply {
            fabAddShoppingItem.setOnClickListener {
                findNavController().navigate(
                    ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItem()
                )
            }
        }

        subscribeToObservers()
        setupRecyclerView()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentShoppingBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val itemTouchCallBack = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder,
        ): Boolean {
//          /* NO-OP */
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[pos]
            viewModel.deleteShoppingItem(item)

            Snackbar.make(requireView(), "Item deleted Successfully", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel.insertShoppingItemIntoDB(item)
                }
                show()
            }

        }
    }

    private fun setupRecyclerView() {
        binding.rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallBack).attachToRecyclerView(this)
        }
    }

    private fun subscribeToObservers() {
        viewModel.shoppingItems.observe(viewLifecycleOwner) {
            shoppingItemAdapter.shoppingItems = it
        }
        viewModel.totalPrice.observe(viewLifecycleOwner) {
            val price = it ?: 0f
            val priceText = "Total Price : $price $"
            binding.tvShoppingItemPrice.text = priceText
        }
    }
}