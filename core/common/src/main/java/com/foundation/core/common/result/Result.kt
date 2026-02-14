package com.foundation.core.common.result

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * 데이터 레이어에서 UI 레이어까지 일관된 상태 전달을 위한 공통 래퍼.
 *
 * @param T 성공 시 전달되는 데이터 타입.
 */
sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable) : Result<Nothing>
    data object Loading : Result<Nothing>
}

/**
 * [Flow]를 [Result]로 감싸는 확장 함수.
 *
 * 'Loading' → 'Success' 또는 'Error' 순서로 방출한다.
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> =
    map<T, Result<T>> { Result.Success(it) }
        .onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
