package com.rasel.androidbaseapp.domain.interactor


import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rasel.androidbaseapp.domain.repository.SettingsRepository
import com.rasel.androidbaseapp.fakes.FakeData
import com.rasel.androidbaseapp.utils.DomainBaseTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetSettingsTestDomain : DomainBaseTest() {

    @Mock
    lateinit var settingRepository: SettingsRepository

    lateinit var sut: GetSettingsUseCase

    @Before
    fun setUp() {
        sut = GetSettingsUseCase(settingRepository)
    }

    @Test
    fun `get settings with night mode on should return success result settings list`() = runTest {
            // Arrange (Given)
            val isNightMode = true
            whenever(settingRepository.getSettings(isNightMode)) doReturn FakeData.getSettings(
                isNightMode
            )

            // Act (When)
            val settings = sut(isNightMode).single()

            // Assert (Then)
            assertEquals(settings.size, 3)
            assertTrue(settings[0].selectedValue)
            verify(settingRepository, times(1)).getSettings(isNightMode)
        }

    @Test
    fun `get settings with night mode off should return success result settings list`() = runTest {
            // Arrange (Given)
            val isNightMode = false
            whenever(settingRepository.getSettings(isNightMode)) doReturn FakeData.getSettings(
                isNightMode
            )

            // Act (When)
            val settings = sut(isNightMode).single()

            // Assert (Then)
            assertEquals(settings.size, 3)
            assertFalse(settings[0].selectedValue)
            verify(settingRepository, times(1)).getSettings(isNightMode)
        }

    @Test
    fun `get settings with night mode off return error result with exception`() = runTest {
            // Arrange (Given)
            val isNightMode = false
            whenever(settingRepository.getSettings(isNightMode)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut(isNightMode).single() }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(),
                instanceOf(IOException::class.java)
            )
            verify(settingRepository, times(1)).getSettings(isNightMode)
        }
}
