package com.chaudharynabin6.shoppinglisttesting.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.chaudharynabin6.shoppinglisttesting.R
import com.chaudharynabin6.shoppinglisttesting.adapter.ImageAdapter
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

@HiltAndroidTest
@MediumTest
@ExperimentalCoroutinesApi
class ImagePickFragmentTest {

    @get:Rule
    var hiltAndroidRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var shoppingFragmentFactory: ShoppingFragmentFactory

    @Inject
    lateinit var navController: NavController

    @Before
    fun setup() {
        hiltAndroidRule.inject()
    }

    @Test
    fun clickImage_____popBackStack_and_SetImageUrl() {
        val imageUrl = "test"
        val testViewModel = ShoppingViewModel(FakeShoppingRepository())

        launchFragmentInHiltContainer<ImagePickFragment>(
            fragmentFactory = shoppingFragmentFactory
        ) {
            Navigation.setViewNavController(requireView(), navController)
            imageAdapter.images = listOf(imageUrl)
            shoppingViewModel = testViewModel
        }

        onView(withId(R.id.rvImages)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageAdapter.ImageViewHolder>(
                0,
                click()
            )
        )

        verify(navController).popBackStack()
        assertThat(testViewModel.curImageUrl.getOrAwaitValueAndroidTest()).isEqualTo(imageUrl)
    }


}