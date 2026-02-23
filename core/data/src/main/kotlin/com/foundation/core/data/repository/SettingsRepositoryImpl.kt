package com.foundation.core.data.repository

import com.foundation.core.data.datastore.SettingsDataSource
import com.foundation.core.domain.repository.SettingsRepository
import com.foundation.core.model.UserSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [SettingsRepository]의 구현체.
 *
 * [SettingsDataSource]에 작업을 위임하며,
 * 도메인 레이어와 데이터 레이어 사이의 경계를 담당한다.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataSource: SettingsDataSource,
) : SettingsRepository {

    override val userSettings: Flow<UserSettings> = dataSource.userSettings

    override suspend fun updateLastLaunchedAt(launchedAt: Long) {
        dataSource.updateLastLaunchedAt(launchedAt)
    }
}
