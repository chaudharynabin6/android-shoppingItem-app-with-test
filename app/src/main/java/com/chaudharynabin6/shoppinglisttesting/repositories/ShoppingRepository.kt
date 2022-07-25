package com.chaudharynabin6.shoppinglisttesting.repositories

import androidx.lifecycle.LiveData
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItem
import com.chaudharynabin6.shoppinglisttesting.data.remote.responses.ImageResponse
import com.chaudharynabin6.shoppinglisttesting.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery : String) : Resource<ImageResponse>

}