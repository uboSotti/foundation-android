package com.foundation.core.database.di

import android.content.Context
import androidx.room.Room
import com.foundation.core.database.FoundationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): FoundationDatabase {
        return Room.databaseBuilder(
            context,
            FoundationDatabase::class.java,
            "foundation-database",
        ).build()
    }
}
