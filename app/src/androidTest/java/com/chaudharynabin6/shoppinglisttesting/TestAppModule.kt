package com.chaudharynabin6.shoppinglisttesting

import android.content.Context
import androidx.navigation.NavController
import androidx.room.Room
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItemDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import org.mockito.Mockito.mock
import javax.inject.Named
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDB(
        @ApplicationContext context: Context,
    ): ShoppingItemDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            ShoppingItemDatabase::class.java
        ).allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideNavController(): NavController {
        return mock(NavController::class.java)
    }

    @Provides
    @Singleton
    @Named("test")
    fun providesDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

}