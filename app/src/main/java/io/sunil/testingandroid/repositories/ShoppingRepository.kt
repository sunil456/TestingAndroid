package io.sunil.testingandroid.repositories

import androidx.lifecycle.LiveData
import io.sunil.testingandroid.data.local.ShoppingItem
import io.sunil.testingandroid.data.remote.responses.ImageResponse
import io.sunil.testingandroid.other.Resource

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems() : LiveData<List<ShoppingItem>>

    fun observeTotalPrice() : LiveData<Float>

    suspend fun searchForImage(imageQuery: String) : Resource<ImageResponse>

}