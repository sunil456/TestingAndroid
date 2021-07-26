package io.sunil.testingandroid.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import io.sunil.testingandroid.MainCoroutineRule
import io.sunil.testingandroid.getOrAwaitValueTest
import io.sunil.testingandroid.other.Constants
import io.sunil.testingandroid.other.Status
import io.sunil.testingandroid.repositories.FakeShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest{


    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var shoppingViewModel: ShoppingViewModel

    @Before
    fun setup(){
        shoppingViewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, return error`(){
        shoppingViewModel.insertShoppingItem(name = "sunil",amountString = "", priceString = "3.0")

        val value = shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert shopping item with too long name, return error`(){

        val stringName = buildString {
            for(i in 1..Constants.MAX_NAME_LENGTH + 1){
                append(1)
            }
        }

        shoppingViewModel.insertShoppingItem(name = stringName,amountString = "5", priceString = "3.0")

        val value = shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, return error`(){

        val stringPrice = buildString {
            for(i in 1..Constants.MAX_PRICE_LENGTH + 1){
                append(1)
            }
        }

        shoppingViewModel.insertShoppingItem(name = "stringName",amountString = "5", priceString = stringPrice)

        val value = shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, return error`(){

        shoppingViewModel.insertShoppingItem(name = "stringName",amountString = "9999999999999999999999999999", priceString = "3.0")

        val value = shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, return success`(){

        shoppingViewModel.insertShoppingItem(name = "stringName",amountString = "8", priceString = "3.0")

        val value = shoppingViewModel.insertShoppingItemStatus.getOrAwaitValueTest()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
}