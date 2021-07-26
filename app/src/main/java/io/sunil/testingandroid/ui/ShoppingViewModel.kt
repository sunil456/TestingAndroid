package io.sunil.testingandroid.ui


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.sunil.testingandroid.data.local.ShoppingItem
import io.sunil.testingandroid.data.remote.responses.ImageResponse
import io.sunil.testingandroid.other.Constants
import io.sunil.testingandroid.other.Resource
import io.sunil.testingandroid.other.Event
import io.sunil.testingandroid.repositories.ShoppingRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()

    val totalPrice = repository.observeTotalPrice()

    private val _image = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _image

    private val _curlImageUrl = MutableLiveData<String>()
    val curlImageUrl: LiveData<String> = _curlImageUrl

    // This is for inserting data to Database
    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> = _insertShoppingItemStatus

    private fun setCurrentImageUrl(url: String){
        _curlImageUrl.postValue(url)
    }

    fun deleteSHoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    private fun insertShoppingItemToDatabase(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String){

        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty())
        {
            _insertShoppingItemStatus.postValue(Event(Resource.error("The field must not be empty", data = null)))
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error("The Name of the item " +
                    "must not exceed ${Constants.MAX_NAME_LENGTH} characters", data = null)))
            return
        }

        if (priceString.length > Constants.MAX_PRICE_LENGTH){
            _insertShoppingItemStatus.postValue(Event(Resource.error("The Price of the item " +
                    "must not exceed ${Constants.MAX_PRICE_LENGTH} characters", data = null)))
            return
        }
        val amount = try {
            amountString.toInt()
        }catch (e : Exception){
            _insertShoppingItemStatus.postValue(Event(Resource.error("Please enter valid amount", data = null)))
            return
        }

        val shoppingItem = ShoppingItem(
            name = name,
            price = priceString.toFloat(),
            amount = amountString.toInt(),
            imageUrl = _curlImageUrl.value ?: "")

        insertShoppingItemToDatabase(shoppingItem = shoppingItem)
        setCurrentImageUrl("")

        _insertShoppingItemStatus.postValue(Event(Resource.success(data = shoppingItem)))

    }

//    fun searchFormImage(imageQuery: String) = viewModelScope.launch {
//        repository.searchForImage(imageQuery)
//    }

    fun searchFormImage(imageQuery: String){

        if (imageQuery.isEmpty())
            return
        _image.value = Event(Resource.loading(data = null))

        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _image.value = Event(response)
        }

    }




}