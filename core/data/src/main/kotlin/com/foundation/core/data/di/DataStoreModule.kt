package com.foundation.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.foundation.core.data.repository.SettingsRepositoryImpl
import com.foundation.core.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** 앱 전역 DataStore 인스턴스. 파일명: "foundation_settings" */
private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "foundation_settings",
)

/**
 * DataStore 및 Settings 관련 의존성을 제공하는 Hilt 모듈.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DataStoreModule {

    /**
     * [SettingsRepositoryImpl]을 [SettingsRepository] 인터페이스에 바인딩한다.
     */
    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        impl: SettingsRepositoryImpl,
    ): SettingsRepository

    companion object {

        /**
         * 앱 전역 싱글톤 [DataStore] 인스턴스를 제공한다.
         *
         * `preferencesDataStore` 위임 프로퍼티를 통해 동일 파일에 대한
         * 인스턴스가 하나만 생성됨을 보장한다.
         */
        @Provides
        @Singleton
        fun provideSettingsDataStore(
            @ApplicationContext context: Context,
        ): DataStore<Preferences> = context.settingsDataStore
    }
}
