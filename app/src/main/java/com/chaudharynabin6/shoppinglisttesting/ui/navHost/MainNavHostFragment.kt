package com.chaudharynabin6.shoppinglisttesting.ui.navHost

import android.content.Context
import androidx.navigation.fragment.NavHostFragment
import com.chaudharynabin6.shoppinglisttesting.ui.ShoppingFragmentFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainNavHostFragment : NavHostFragment() {

    @Inject
    lateinit var shoppingFragmentFactory: ShoppingFragmentFactory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        childFragmentManager.fragmentFactory = shoppingFragmentFactory
    }
}