package com.foundation.core.data.repository

import app.cash.turbine.test
import com.foundation.core.common.error.AppError
import com.foundation.core.common.error.AppException
import com.foundation.core.data.datastore.SettingsDataSource
import com.foundation.core.model.UserSettings
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class SettingsRepositoryImplTest {

    @Test
    fun `userSettings는 DataSource의 데이터를 그대로 전달한다`() = runTest {
        val dataSource = mockk<SettingsDataSource>()
        every { dataSource.userSettings } returns flowOf(
            UserSettings(lastLaunchedAt = 1000L),
        )

        val repository = SettingsRepositoryImpl(dataSource)

        repository.userSettings.test {
            val settings = awaitItem()
            assertEquals(1000L, settings.lastLaunchedAt)
            awaitComplete()
        }
    }

    @Test
    fun `DataSource에서 예외 발생 시 AppError Storage DataStore로 변환된다`() = runTest {
        val dataSource = mockk<SettingsDataSource>()
        every { dataSource.userSettings } returns flow {
            throw IOException("DataStore corrupted")
        }

        val repository = SettingsRepositoryImpl(dataSource)

        repository.userSettings.test {
            val error = awaitError()
            assertTrue(error is AppException)
            assertTrue((error as AppException).error is AppError.Storage.DataStore)
        }
    }

    @Test
    fun `updateLastLaunchedAt는 DataSource에 위임한다`() = runTest {
        val dataSource = mockk<SettingsDataSource>(relaxUnitFun = true)
        every { dataSource.userSettings } returns flowOf()

        val repository = SettingsRepositoryImpl(dataSource)

        repository.updateLastLaunchedAt(5000L)

        coVerify { dataSource.updateLastLaunchedAt(5000L) }
    }
}
