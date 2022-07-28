package com.chaudharynabin6.shoppinglisttesting.repositories

import androidx.lifecycle.LiveData
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingDao
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItem
import com.chaudharynabin6.shoppinglisttesting.data.remote.PixabayAPI
import com.chaudharynabin6.shoppinglisttesting.data.remote.responses.ImageResponse
import com.chaudharynabin6.shoppinglisttesting.other.Resource
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI,
) : ShoppingRepository {
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {

        return try {
            val response = pixabayAPI.searchForImage(imageQuery)
            val resource = if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(data = it)
                }
                    ?: Resource.error(msg = "An unknown error occurred", data = null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
            resource
        } catch (
            e: Exception,
        ) {
            e.printStackTrace()
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }
}