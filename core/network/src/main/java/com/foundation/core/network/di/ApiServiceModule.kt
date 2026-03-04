package com.foundation.core.network.di

import com.foundation.core.network.api.GithubApiService
import com.foundation.core.network.host.GithubHost
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/** Retrofit API 서비스 인스턴스를 제공하는 Hilt 모듈. */
@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {

    @Provides
    @Singleton
    fun provideGithubApiService(
        githubHost: GithubHost,
        okHttpClient: OkHttpClient,
        json: Json,
    ): GithubApiService {
        return Retrofit.Builder()
            .baseUrl(githubHost.url)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(GithubApiService::class.java)
    }
}
