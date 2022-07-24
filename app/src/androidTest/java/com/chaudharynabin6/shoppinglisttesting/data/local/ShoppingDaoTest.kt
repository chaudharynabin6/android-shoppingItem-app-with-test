package com.chaudharynabin6.shoppinglisttesting.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.chaudharynabin6.shoppinglisttesting.utils.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

// instrumented unit test
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class ShoppingDaoTest {
    private lateinit var database: ShoppingItemDatabase
    private lateinit var dao: ShoppingDao

    //    to execute the all function one after another in single thread
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ShoppingItemDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.shoppingDao()
    }

    @After
    fun tearDown() {
        database.close()
    }


    @Test
    fun insertShoppingItem() {
//        depreciated
//        runBlockingTest {
//
//        }
//        runTest is used for running test in single coroutine
        runTest {
            val shoppingItem = ShoppingItem(
                name = "name",
                amount = 1,
                price = 1f,
                imageUrl = "imageUrl",
                id = 1
            )

            dao.insertShoppingItem(shoppingItem)
            val shoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

            assertThat(shoppingItems).contains(shoppingItem)
        }
    }

    @Test
    fun deleteShoppingItem() = runTest {
        val shoppingItem = ShoppingItem(
            name = "name",
            amount = 1,
            price = 1f,
            imageUrl = "imageUrl",
            id = 1
        )
//      insertion test
        dao.insertShoppingItem(shoppingItem)
        val afterInsertionShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(afterInsertionShoppingItems).contains(shoppingItem)

//       deletion test
        dao.deleteShoppingItem(shoppingItem)
        val afterDeletionShoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()
        assertThat(afterDeletionShoppingItems).doesNotContain(shoppingItem)
    }

    @Test
    fun observeAllShoppingItems() = runTest {
        val item1 = ShoppingItem(
            "name",
            1,
            1f,
            "imageUrl",
            1
        )
        val item2 = ShoppingItem(
            "name",
            2,
            1f,
            "imageUrl",
            2
        )
        val item3 = ShoppingItem(
            "name",
            3,
            1f,
            "imageUrl",
            3
        )
        dao.insertShoppingItem(item1)
        dao.insertShoppingItem(item2)
        dao.insertShoppingItem(item3)

        val shoppingItems = dao.observeAllShoppingItems().getOrAwaitValue()

        assertThat(shoppingItems).containsExactly(item1,item2,item3)
    }

    @Test
    fun observeTotalPrice() = runTest {
        val item1 = ShoppingItem(
            "name",
            2,
            2f,
            "imageUrl",
            1
        )
        val item2 = ShoppingItem(
            "name",
            3,
            3f,
            "imageUrl",
            2
        )
        val item3 = ShoppingItem(
            "name",
            5,
            5f,
            "imageUrl",
            3
        )
        dao.insertShoppingItem(item1)
        dao.insertShoppingItem(item2)
        dao.insertShoppingItem(item3)

       val totalPrice =  dao.observeTotalPrice().getOrAwaitValue()
        assertThat(totalPrice).isEqualTo(2*2f + 3*3f + 5*5f)
    }
}

