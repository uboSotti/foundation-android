package com.foundation.core.common.result

import app.cash.turbine.test
import com.foundation.core.common.error.AppError
import com.foundation.core.common.error.AppException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ResultTest {

    @Test
    fun `asResult는 Loading을 먼저 방출하고 Success를 방출한다`() = runTest {
        flowOf("data").asResult().test {
            assertTrue(awaitItem() is Result.Loading)

            val success = awaitItem()
            assertTrue(success is Result.Success)
            assertEquals("data", (success as Result.Success).data)

            awaitComplete()
        }
    }

    @Test
    fun `AppException 발생 시 내부 AppError를 추출하여 Error로 방출한다`() = runTest {
        flow<String> {
            throw AppException(AppError.Network.Http(code = 500))
        }.asResult().test {
            assertTrue(awaitItem() is Result.Loading)

            val error = awaitItem()
            assertTrue(error is Result.Error)
            val appError = (error as Result.Error).error
            assertTrue(appError is AppError.Network.Http)
            assertEquals(500, (appError as AppError.Network.Http).code)

            awaitComplete()
        }
    }

    @Test
    fun `일반 예외 발생 시 AppError Unknown으로 감싸서 Error를 방출한다`() = runTest {
        flow<String> {
            throw RuntimeException("unexpected")
        }.asResult().test {
            assertTrue(awaitItem() is Result.Loading)

            val error = awaitItem()
            assertTrue(error is Result.Error)
            val appError = (error as Result.Error).error
            assertTrue(appError is AppError.Unknown)
            assertEquals("unexpected", appError.message)

            awaitComplete()
        }
    }

    @Test
    fun `여러 값을 방출하는 Flow는 각각 Success로 래핑된다`() = runTest {
        flowOf(1, 2, 3).asResult().test {
            assertTrue(awaitItem() is Result.Loading)

            assertEquals(1, (awaitItem() as Result.Success).data)
            assertEquals(2, (awaitItem() as Result.Success).data)
            assertEquals(3, (awaitItem() as Result.Success).data)

            awaitComplete()
        }
    }
}
