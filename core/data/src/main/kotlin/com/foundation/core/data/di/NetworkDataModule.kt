package com.foundation.core.data.di

import com.foundation.core.data.repository.GithubRepositoryImpl
import com.foundation.core.domain.repository.GithubRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** 네트워크 기반 Repository 바인딩을 제공하는 Hilt 모듈. */
@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkDataModule {

    @Binds
    @Singleton
    abstract fun bindGithubRepository(
        impl: GithubRepositoryImpl,
    ): GithubRepository
}
