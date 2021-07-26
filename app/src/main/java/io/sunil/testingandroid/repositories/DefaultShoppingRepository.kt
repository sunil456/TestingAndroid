package io.sunil.testingandroid.repositories

import androidx.lifecycle.LiveData
import io.sunil.testingandroid.data.local.ShoppingDao
import io.sunil.testingandroid.data.local.ShoppingItem
import io.sunil.testingandroid.data.remote.PixabayAPI
import io.sunil.testingandroid.data.remote.responses.ImageResponse
import io.sunil.testingandroid.other.Resource
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixabayAPI: PixabayAPI
) : ShoppingRepository {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        return shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        return shoppingDao.deleteShoppingItem(shoppingItem)
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

            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error(response.message(), data = null)
            }
            else{
                Resource.error("An unknown error occurred", data = null)
            }

        } catch (e: Exception){
            Resource.error(message = e.message, data = null)
        }
    }
}