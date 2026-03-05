package com.foundation.core.network.util

import com.foundation.core.common.error.AppError
import com.foundation.core.common.error.AppException
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/**
 * API 호출을 감싸서 플랫폼 예외를 [AppError]로 변환하는 유틸리티.
 *
 * [HttpException], [kotlinx.serialization.SerializationException], [IOException]를
 * 각각 적절한 [AppError.Network] 하위 타입으로 매핑하여 [AppException]으로 throw한다.
 *
 * @param block 실행할 suspend API 호출.
 * @return API 호출 결과.
 * @throws AppException 변환된 에러를 감싼 예외.
 */
suspend fun <T> safeApiCall(block: suspend () -> T): T {
    try {
        return block()
    } catch (e: HttpException) {
        throw AppException(AppError.Network.Http(code = e.code(), cause = e))
    } catch (e: SerializationException) {
        throw AppException(AppError.Network.Serialization(cause = e))
    } catch (e: IOException) {
        throw AppException(AppError.Network.Connection(cause = e))
    }
}
