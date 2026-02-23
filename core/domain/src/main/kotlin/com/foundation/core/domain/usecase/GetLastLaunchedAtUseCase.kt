package com.foundation.core.domain.usecase

import com.foundation.core.common.result.Result
import com.foundation.core.common.result.asResult
import com.foundation.core.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * 앱의 마지막 실행 시각을 가져오는 UseCase.
 *
 * [SettingsRepository.userSettings]에서 [Long?] 값만 추출하여
 * [Result]로 래핑한 Flow를 반환한다.
 *
 * - [Result.Loading]: 초기 데이터 로딩 중
 * - [Result.Success]: 정상적으로 값을 수신 (null = 최초 실행)
 * - [Result.Error]: DataStore 읽기 실패
 */
class GetLastLaunchedAtUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository,
) {
    /** UseCase를 invoke 연산자로 호출한다. */
    operator fun invoke(): Flow<Result<Long?>> =
        settingsRepository.userSettings
            .map { it.lastLaunchedAt }
            .asResult()
}
