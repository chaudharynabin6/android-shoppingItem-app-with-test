package com.chaudharynabin6.shoppinglisttesting.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.chaudharynabin6.shoppinglisttesting.adapter.ImageAdapter
import com.chaudharynabin6.shoppinglisttesting.adapter.ShoppingItemAdapter
import javax.inject.Inject


class ShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val shoppingItemAdapter: ShoppingItemAdapter,
    private val glide: RequestManager,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItem::class.java.name -> AddShoppingItem(glide
            )
            ShoppingFragment::class.java.name -> ShoppingFragment(
                shoppingItemAdapter
            )
            else -> return super.instantiate(classLoader, className)
        }

    }
}