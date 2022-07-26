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
import org.mockito.Mockito.mock
import javax.inject.Named

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

}