package com.chaudharynabin6.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.other.launchFragmentInHiltContainer
import com.chaudharynabin6.shoppinglisttesting.repositories.FakeShoppingRepository
import com.chaudharynabin6.shoppinglisttesting.ui.viewmodels.ShoppingViewModel
import com.chaudharynabin6.shoppinglisttesting.utils.getOrAwaitValue
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


    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var navController: NavController

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun ivShoppingImage_click__________navigateTo_ImagePickFragment() {
        launchFragmentInHiltContainer<AddShoppingItem> {
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
        launchFragmentInHiltContainer<AddShoppingItem> {
            shoppingViewModel = viewModel
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()

        assertThat(viewModel.curImageUrl.getOrAwaitValue()).isEqualTo("")

    }

    @Test
    fun ivShoppingImage_click__________popUpBackStack() {

        launchFragmentInHiltContainer<AddShoppingItem> {
            Navigation.setViewNavController(requireView(), navController)
        }

        pressBack()


        verify(navController).popBackStack()
    }
}