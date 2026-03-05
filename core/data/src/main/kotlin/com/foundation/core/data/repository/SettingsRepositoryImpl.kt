package com.foundation.core.data.repository

import com.foundation.core.common.error.AppError
import com.foundation.core.common.error.AppException
import com.foundation.core.data.datastore.SettingsDataSource
import com.foundation.core.domain.repository.SettingsRepository
import com.foundation.core.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject
import javax.inject.Singleton

/**
 * [SettingsRepository]의 구현체.
 *
 * [SettingsDataSource]에 작업을 위임하며,
 * 도메인 레이어와 데이터 레이어 사이의 경계를 담당한다.
 *
 * DataStore에서 발생하는 예외를 [AppError.Storage.DataStore]로 변환한다.
 */
@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataSource: SettingsDataSource,
) : SettingsRepository {

    override val userSettings: Flow<UserSettings> = dataSource.userSettings
        .catch { throw AppException(AppError.Storage.DataStore(cause = it)) }

    override suspend fun updateLastLaunchedAt(launchedAt: Long) {
        dataSource.updateLastLaunchedAt(launchedAt)
    }
}
