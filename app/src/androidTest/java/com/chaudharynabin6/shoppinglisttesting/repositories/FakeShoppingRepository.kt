@file:Suppress("UNCHECKED_CAST")

package com.chaudharynabin6.shoppinglisttesting.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItem
import com.chaudharynabin6.shoppinglisttesting.data.remote.responses.ImageResponse
import com.chaudharynabin6.shoppinglisttesting.other.Resource
import kotlin.properties.Delegates

class FakeShoppingRepository : ShoppingRepository {

    private var shoppingItems: MutableList<ShoppingItem> by Delegates.observable(
        mutableListOf<ShoppingItem>()
    ) { _, _, newValue ->
        observableShoppingItems.postValue(newValue)
        observableTotalPrice.postValue(getTotalPrice(newValue))
    }

    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(listOf())
    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun getTotalPrice(shoppingItems: List<ShoppingItem>): Float {
        return shoppingItems.sumOf {
            it.price.toDouble() * it.amount.toDouble()
        }.toFloat()
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.add(shoppingItem).also {
            // refreshLiveDAta
            shoppingItems = shoppingItems
        }

    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingItems.remove(shoppingItem).also {
            // refreshLiveDAta
            shoppingItems = shoppingItems
        }


    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(ImageResponse(listOf(), 0, 0))
        }
    }
}


