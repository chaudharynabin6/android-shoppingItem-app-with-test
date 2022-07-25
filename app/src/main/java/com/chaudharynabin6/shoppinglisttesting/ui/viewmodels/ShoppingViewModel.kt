package com.chaudharynabin6.shoppinglisttesting.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItem
import com.chaudharynabin6.shoppinglisttesting.data.remote.responses.ImageResponse
import com.chaudharynabin6.shoppinglisttesting.other.Constants
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

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus

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

        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The field must not be empty",
                null)))
            return
        }

        if (name.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(Resource.error("The name of the item must not exceed ${Constants.MAX_NAME_LENGTH}",
                    null))
            )
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The price of the item" +
                    "must not exceed ${Constants.MAX_PRICE_LENGTH} characters", null)))
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Please enter a valid amount",
                null)))
            return
        }
        val price = try {
            priceString.toFloat()
        } catch (e: Exception) {
            _insertShoppingItemStatus.postValue(Event(Resource.error("Please enter a valid price",
                null)))
            return
        }

        val shoppingItem =
            ShoppingItem(name, amount, price, _curImageUrl.value ?: "")
        insertShoppingItemIntoDB(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    fun searchImage(imageQuery: String) {

        if (imageQuery.isEmpty()) {
            return
        }
        _images.postValue(Event(Resource.loading(null)))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _images.postValue(Event(response))
        }
    }
}