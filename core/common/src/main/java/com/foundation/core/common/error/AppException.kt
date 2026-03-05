package com.foundation.core.common.error

/**
 * [AppError]를 감싸는 예외 래퍼.
 *
 * Repository에서 플랫폼 예외를 [AppError]로 변환한 뒤 이 예외로 감싸서 throw하면,
 * [com.foundation.core.common.result.asResult]가 이를 감지하여 [AppError]를 추출한다.
 */
class AppException(val error: AppError) : Exception(error.message, error.cause)
