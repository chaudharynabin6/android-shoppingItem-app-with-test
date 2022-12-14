package com.chaudharynabin6.shoppinglisttesting.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItem
import com.chaudharynabin6.shoppinglisttesting.other.Status
import com.chaudharynabin6.shoppinglisttesting.utils.getOrAwaitValueAndroidTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class FakeShoppingRepositoryTest {
    private lateinit var fakeShoppingRepository: FakeShoppingRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Before
    fun setup() {
        fakeShoppingRepository = FakeShoppingRepository()

    }

    @Test
    fun insertShoppingItem_And_ObserveAllShoppingItems() {
        runTest {
            val shoppingItem = ShoppingItem(
                "name",
                1,
                1f,
                "url",
                1
            )
            fakeShoppingRepository.insertShoppingItem(shoppingItem)

            val shoppingItems = fakeShoppingRepository.observeAllShoppingItems().getOrAwaitValueAndroidTest()

            assertThat(shoppingItems).contains(shoppingItem)
        }
    }

    @Test
    fun deleteShoppingItem() {
        runTest {
            val shoppingItem = ShoppingItem(
                "name",
                1,
                1f,
                "url",
                1
            )
            fakeShoppingRepository.insertShoppingItem(shoppingItem)

            val shoppingItemsBeforeDelete =
                fakeShoppingRepository.observeAllShoppingItems().getOrAwaitValueAndroidTest()
//            shopping item before delete
            assertThat(shoppingItemsBeforeDelete).contains(shoppingItem)

            fakeShoppingRepository.deleteShoppingItem(shoppingItem)

            val shoppingItemsAfterDeletion =
                fakeShoppingRepository.observeAllShoppingItems().getOrAwaitValueAndroidTest()
//            shopping item after deletion
            assertThat(shoppingItemsAfterDeletion).doesNotContain(shoppingItem)

        }
    }

    @Test
    fun searchForImage_return_ResourceSuccess() {
        runTest {
            fakeShoppingRepository.setShouldReturnNetworkError(false)
            val resourceSuccess = fakeShoppingRepository.searchForImage("q")
            assertThat(resourceSuccess.status).isEqualTo(Status.SUCCESS)
        }
    }

    @Test
    fun searchForImage_return_ResourceError() {
        runTest {
            fakeShoppingRepository.setShouldReturnNetworkError(true)
            val resourceError = fakeShoppingRepository.searchForImage("q")
            assertThat(resourceError.status).isEqualTo(Status.ERROR)
        }
    }

    @Test
    fun observeTotalPrice() {
        runTest {
            val shoppingItem1 = ShoppingItem(
                "name",
                2,
                2f,
                "url",
                1
            )
            val shoppingItem2 = ShoppingItem(
                "name",
                3,
                3f,
                "url",
                2
            )
            fakeShoppingRepository.insertShoppingItem(shoppingItem1)
            fakeShoppingRepository.insertShoppingItem(shoppingItem2)

            val totalPrice = fakeShoppingRepository.observeTotalPrice().getOrAwaitValueAndroidTest()

            assertThat(totalPrice).isEqualTo(2 * 2f + 3 * 3f)
        }
    }


}