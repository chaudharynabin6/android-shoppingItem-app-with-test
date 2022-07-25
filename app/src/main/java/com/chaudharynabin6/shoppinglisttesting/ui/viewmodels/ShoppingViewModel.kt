package com.chaudharynabin6.shoppinglisttesting.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItem
import com.chaudharynabin6.shoppinglisttesting.data.remote.responses.ImageResponse
import com.chaudharynabin6.shoppinglisttesting.other.Event
import com.chaudharynabin6.shoppinglisttesting.other.Resource
import com.chaudharynabin6.shoppinglisttesting.repositories.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository,
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _images = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _images

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteShoppingItem(shoppingItem)
        }
    }

    fun insertShoppingItemIntoDB(shoppingItem: ShoppingItem) {
        viewModelScope.launch(Dispatchers.IO)
        {
            repository.insertShoppingItem(shoppingItem)
        }
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {

    }

    fun searchImage(imageQuery: String) {

    }
}