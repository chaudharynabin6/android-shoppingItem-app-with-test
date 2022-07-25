package com.chaudharynabin6.shoppinglisttesting.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ImagePickFragment : Fragment(R.layout.fragment_image_pick) {
    private val shoppingViewModel: ShoppingViewModel by viewModels()


}