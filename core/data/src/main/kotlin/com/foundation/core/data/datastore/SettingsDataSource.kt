package com.foundation.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.foundation.core.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore를 직접 읽고 쓰는 로컬 데이터 소스.
 *
 * Repository와 DataStore 사이의 어댑터 역할을 하며,
 * Preferences Key 관리 및 직렬화/역직렬화를 담당한다.
 */
@Singleton
class SettingsDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {

    /** [UserSettings]를 실시간으로 관찰하는 Flow. */
    val userSettings: Flow<UserSettings> = dataStore.data.map { preferences ->
        UserSettings(
            lastLaunchedAt = preferences[PreferencesKeys.LAST_LAUNCHED_AT],
        )
    }

    /**
     * 앱 마지막 실행 시각을 DataStore에 저장한다.
     *
     * @param launchedAt 저장할 시각 (Unix epoch millis).
     */
    suspend fun updateLastLaunchedAt(launchedAt: Long) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.LAST_LAUNCHED_AT] = launchedAt
        }
    }

    private object PreferencesKeys {
        val LAST_LAUNCHED_AT = longPreferencesKey("last_launched_at")
    }
}
