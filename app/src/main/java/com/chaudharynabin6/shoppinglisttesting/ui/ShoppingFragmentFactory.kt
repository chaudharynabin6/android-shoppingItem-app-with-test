package com.chaudharynabin6.shoppinglisttesting.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.chaudharynabin6.shoppinglisttesting.adapter.ImageAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


class ShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            else -> return super.instantiate(classLoader, className)
        }

    }
}