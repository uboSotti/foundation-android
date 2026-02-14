package com.foundation.core.network.di

import com.foundation.core.common.di.BaseUrl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DebugNetworkModule {

    @Provides
    @Singleton
    @IntoSet
    fun provideHttpLoggingInterceptor(
        @BaseUrl baseUrl: String,
    ): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = if (baseUrl.isNotEmpty()) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
}
