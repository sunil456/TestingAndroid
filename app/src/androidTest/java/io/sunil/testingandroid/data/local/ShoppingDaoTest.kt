

package io.sunil.testingandroid.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.sunil.testingandroid.getOrAwaitValue
import io.sunil.testingandroid.launchFragmentInHiltContainer
import io.sunil.testingandroid.ui.ShoppingFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class)
@SmallTest
@HiltAndroidTest
class ShoppingDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instanceTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var shoppingItemDatabase: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    @Before
    fun setup()
    {
//        shoppingItemDatabase = Room.inMemoryDatabaseBuilder(
//            ApplicationProvider.getApplicationContext(),
//            ShoppingItemDatabase::class.java
//        ).allowMainThreadQueries().build()
        hiltRule.inject()

        dao = shoppingItemDatabase.shoppingDao()
    }

    @After
    fun teardown()
    {
        shoppingItemDatabase.close()
    }

    @Test
    fun testLaunchFragmentInHiltContainer(){
        launchFragmentInHiltContainer<ShoppingFragment> {

        }
    }

    @Test
    fun insertShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem(id = 1, name = "test", amount = 1, price = 1f, imageUrl = "url")
        dao.insertShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).contains(shoppingItem)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val shoppingItem = ShoppingItem(id = 1, name = "test", amount = 1, price = 1f, imageUrl = "url")
        dao.insertShoppingItem(shoppingItem)

        dao.deleteShoppingItem(shoppingItem)

        val allShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(allShoppingItems).doesNotContain(shoppingItem)

    }

    @Test
    fun observeTotalPriceSum() = runBlockingTest {
        val shoppingItem1 = ShoppingItem(id = 1, name = "test", amount = 2, price = 10f, imageUrl = "url")
        val shoppingItem2 = ShoppingItem(id = 2, name = "test", amount = 4, price = 5.5f, imageUrl = "url")
        val shoppingItem3 = ShoppingItem(id = 3, name = "test", amount = 0, price = 100f, imageUrl = "url")
        dao.insertShoppingItem(shoppingItem1)
        dao.insertShoppingItem(shoppingItem2)
        dao.insertShoppingItem(shoppingItem3)

        val totalSumPrice = dao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalSumPrice).isEqualTo(2*10f + 4*5.5f)
    }
}