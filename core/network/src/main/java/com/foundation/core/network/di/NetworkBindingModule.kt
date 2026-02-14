package com.foundation.core.network.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.Multibinds
import okhttp3.Interceptor

@Module
@InstallIn(SingletonComponent::class)
interface NetworkBindingModule {

    @Multibinds
    fun bindInterceptors(): Set<Interceptor>
}
