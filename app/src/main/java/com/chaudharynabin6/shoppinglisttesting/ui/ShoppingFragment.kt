package com.chaudharynabin6.shoppinglisttesting.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItem
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment : Fragment(R.layout.fragment_shopping) {
 private val shoppingViewModel: ShoppingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val shoppingItem = ShoppingItem(
            "name",
            1,
            1f,
            "url"
        )
        shoppingViewModel.insertShoppingItemIntoDB(shoppingItem)
    }
}