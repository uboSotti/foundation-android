package com.foundation.core.network.di

import com.foundation.core.network.host.GithubHost
import com.foundation.core.network.host.Host
import dagger.Binds
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

    @Binds
    fun bindHost(githubHost: GithubHost): Host
}
