package com.chaudharynabin6.shoppinglisttesting

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingDao
import com.chaudharynabin6.shoppinglisttesting.data.local.ShoppingItemDatabase
import com.chaudharynabin6.shoppinglisttesting.data.remote.PixabayAPI
import com.chaudharynabin6.shoppinglisttesting.other.Constants
import com.chaudharynabin6.shoppinglisttesting.repositories.DefaultShoppingRepository
import com.chaudharynabin6.shoppinglisttesting.repositories.ShoppingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context,
    ): ShoppingItemDatabase {
        return Room.databaseBuilder(
            context,
            ShoppingItemDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun prividesShoppingDao(
        shoppingItemDatabase: ShoppingItemDatabase
    ): ShoppingDao {
        return shoppingItemDatabase.shoppingDao()
    }

    @Singleton
    @Provides
    fun providesPixabayAPI(): PixabayAPI {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(
                PixabayAPI::class.java
            )
    }

    @Singleton
    @Provides
    fun providesDefaultShoppingRepository(
        shoppingDao: ShoppingDao,
        pixabayAPI: PixabayAPI,
    ): ShoppingRepository {
        return DefaultShoppingRepository(
            shoppingDao = shoppingDao,
            pixabayAPI = pixabayAPI
        )
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context,
    ): RequestManager {
        return Glide.with(context).setDefaultRequestOptions(
            RequestOptions()
                .placeholder(
                    R.drawable.ic_image
                )
                .error(R.drawable.ic_image)
        )
    }
}