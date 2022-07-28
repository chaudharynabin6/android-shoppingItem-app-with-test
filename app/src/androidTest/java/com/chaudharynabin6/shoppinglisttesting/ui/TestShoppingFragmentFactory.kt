package com.chaudharynabin6.shoppinglisttesting.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.chaudharynabin6.shoppinglisttesting.adapter.ImageAdapter
import com.chaudharynabin6.shoppinglisttesting.adapter.ShoppingItemAdapter
import com.chaudharynabin6.shoppinglisttesting.repositories.FakeShoppingRepository
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
class TestShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val glide: RequestManager,
    private val shoppingItemAdapter: ShoppingItemAdapter,
    @Named("test")
    private val dispatcher: CoroutineDispatcher,
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItem::class.java.name -> AddShoppingItem(glide,
                shoppingViewModel = ShoppingViewModel(FakeShoppingRepository(), dispatcher)
            )
            ShoppingFragment::class.java.name -> ShoppingFragment(
                shoppingItemAdapter,
                ShoppingViewModel(FakeShoppingRepository(), dispatcher)
            )

            else -> super.instantiate(classLoader, className)

        }
    }
}