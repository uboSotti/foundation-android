package com.foundation.core.common.error

/**
 * 앱 전체에서 사용하는 타입 안전한 에러 모델.
 *
 * raw [Throwable] 대신 이 sealed interface를 사용하여
 * UI가 에러 유형에 따라 적절한 메시지를 표시할 수 있도록 한다.
 *
 * @property cause 로깅 및 디버깅을 위한 원본 예외. null일 수 있다.
 * @property message 디버그용 기술 메시지. UI 표시용이 아님.
 */
sealed interface AppError {
    val cause: Throwable?
    val message: String

    /** 네트워크 관련 에러. */
    sealed interface Network : AppError {

        /** 인터넷 연결 없음 또는 타임아웃. */
        data class Connection(
            override val cause: Throwable? = null,
            override val message: String = "Network connection failed",
        ) : Network

        /** HTTP 에러 응답 (4xx, 5xx). */
        data class Http(
            val code: Int,
            override val cause: Throwable? = null,
            override val message: String = "HTTP error $code",
        ) : Network

        /** 응답 파싱/역직렬화 실패. */
        data class Serialization(
            override val cause: Throwable? = null,
            override val message: String = "Failed to parse response",
        ) : Network
    }

    /** 로컬 저장소 관련 에러. */
    sealed interface Storage : AppError {

        /** DataStore 읽기/쓰기 실패. */
        data class DataStore(
            override val cause: Throwable? = null,
            override val message: String = "DataStore operation failed",
        ) : Storage
    }

    /** 분류할 수 없는 에러. */
    data class Unknown(
        override val cause: Throwable? = null,
        override val message: String = cause?.message ?: "Unknown error",
    ) : AppError
}
