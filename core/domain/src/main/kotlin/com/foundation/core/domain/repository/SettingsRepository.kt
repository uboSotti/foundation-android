package com.foundation.core.domain.repository

import com.foundation.core.model.UserSettings
import kotlinx.coroutines.flow.Flow

/**
 * 앱의 사용자 설정을 관리하는 Repository 인터페이스.
 *
 * 구현체는 `:core:data` 모듈에 위치하며, Hilt를 통해 주입된다.
 */
interface SettingsRepository {

    /**
     * 현재 사용자 설정을 실시간으로 관찰하는 Flow.
     *
     * DataStore의 변경 사항이 발생할 때마다 최신 [UserSettings]를 emit한다.
     */
    val userSettings: Flow<UserSettings>

    /**
     * 앱 마지막 실행 시각을 현재 시각으로 갱신한다.
     *
     * @param launchedAt 저장할 시각 (Unix epoch millis).
     */
    suspend fun updateLastLaunchedAt(launchedAt: Long)
}
