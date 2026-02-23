package com.foundation.core.model

/**
 * 앱의 사용자 설정 정보를 담는 도메인 모델.
 *
 * @property lastLaunchedAt 앱을 마지막으로 실행한 시각 (Unix epoch millis).
 *                          최초 실행 전에는 null.
 */
data class UserSettings(
    val lastLaunchedAt: Long? = null,
)
