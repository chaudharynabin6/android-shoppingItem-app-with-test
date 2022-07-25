package com.chaudharynabin6.shoppinglisttesting.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.chaudharynabin6.shoppinglisttesting.other.Constants
import com.chaudharynabin6.shoppinglisttesting.other.Status
import com.chaudharynabin6.shoppinglisttesting.repositories.FakeShoppingRepository
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import com.chaudharynabin6.shoppinglisttesting.utils.MainCoroutineRule
import com.chaudharynabin6.shoppinglisttesting.utils.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ShoppingViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShoppingViewModel

    @Before
    fun setup() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }

    @Test
    fun `insert shopping item with empty field, returns error`() {
//        case 1
        viewModel.insertShoppingItem("name", "", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

//      case 2
        viewModel.insertShoppingItem("name", "3.0", "")

        val value2 = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value2.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

//        case 3
        viewModel.insertShoppingItem("", "3.0", "3.0")

        val value3 = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value3.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)

//        case 4
        viewModel.insertShoppingItem("", "", "")

        val value4 = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value4.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long name, returns error`() {
//        string length is greater than max_name_length
        val string = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem(string, "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too long price, returns error`() {
        val string = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem("name", "5", string)

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with too high amount, returns error`() {
        viewModel.insertShoppingItem("name", "9999999999999999999", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert shopping item with valid input, returns success`() {
        viewModel.insertShoppingItem("name", "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `insert shopping item with valid input, sets the curlImageUrl to empty string`() {

//        setting image url before insertion
        viewModel.setCurImageUrl("url")

//        getting image url before insertion
        val urlBefore = viewModel.curImageUrl.getOrAwaitValue()

//       checking before url
        assertThat(urlBefore).isEqualTo("url")

        viewModel.insertShoppingItem("name", "5", "3.0")

        val value = viewModel.insertShoppingItemStatus.getOrAwaitValue()

//         checking insert successful
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)

//        checking curlImageUrl after insertion is empty or not
        val urlAfterInsertion = viewModel.curImageUrl.getOrAwaitValue()
        assertThat(urlAfterInsertion).isEqualTo("")
    }

    @Test
    fun `setCurImageUrl with url , returns urls`() {
        runTest {
            val urlSet = "url"
            viewModel.setCurImageUrl(urlSet)
            val urlGet = viewModel.curImageUrl.getOrAwaitValue()
            assertThat(urlGet).isEqualTo(urlSet)
        }
    }

}