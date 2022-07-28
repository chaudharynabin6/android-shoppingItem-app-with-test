package com.chaudharynabin6.shoppinglisttesting.ui

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItem
import com.chaudharynabin6.shoppinglisttesting.other.launchFragmentInHiltContainer
import com.chaudharynabin6.shoppinglisttesting.repositories.FakeShoppingRepository
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import com.chaudharynabin6.shoppinglisttesting.utils.getOrAwaitValueAndroidTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify
import javax.inject.Inject


@ExperimentalCoroutinesApi
@HiltAndroidTest
@MediumTest
class AddShoppingItemTest {


    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)


    @get:Rule(order = 2)
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var navController: NavController

    @Inject
    lateinit var testShoppingFragmentFactory: TestShoppingFragmentFactory


    @Before
    fun setup() {
        hiltRule.inject()

    }


    @Test
    fun clickInsertIntoDb______shoppingItemInsertedIntoDb() {


        var testViewModel: ShoppingViewModel? = null

        launchFragmentInHiltContainer<AddShoppingItem>(
            fragmentFactory = testShoppingFragmentFactory
        ) {
            testViewModel = shoppingViewModel
        }
        val shoppingItem = ShoppingItem("shopping item", 5, 5.5f, "", null)
        onView(withId(R.id.etShoppingItemName)).perform(replaceText(shoppingItem.name))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText(shoppingItem.amount.toString()))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText(shoppingItem.price.toString()))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())


        testViewModel?.repository?.javaClass?.name?.let { Log.e("view model", it) }

//            testViewModel?.shoppingItems?.getOrAwaitValueAndroidTest().let {
//                Log.e("shopping items", it.toString())
//            }

        assertThat(testViewModel?.shoppingItems?.getOrAwaitValueAndroidTest()).contains(
            shoppingItem
        )


    }

    @Test
    fun ivShoppingImage_click__________navigateTo_ImagePickFragment() {
        launchFragmentInHiltContainer<AddShoppingItem>(
            fragmentFactory = testShoppingFragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.ivShoppingImage)).perform(
            click()
        )

        verify(navController).navigate(
            AddShoppingItemDirections.actionAddShoppingItemToImagePickFragment()
        )
    }

    @Test
    fun ivShoppingImage_click__________setImageUrl_to_empty() {

        val viewModel = ShoppingViewModel(FakeShoppingRepository())
        launchFragmentInHiltContainer<AddShoppingItem>(
            fragmentFactory = testShoppingFragmentFactory
        ) {
            shoppingViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()

        assertThat(viewModel.curImageUrl.getOrAwaitValueAndroidTest()).isEqualTo("")

    }

    @Test
    fun ivShoppingImage_click__________popUpBackStack() {

        launchFragmentInHiltContainer<AddShoppingItem>(
            fragmentFactory = testShoppingFragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()


        verify(navController).popBackStack()
    }
}