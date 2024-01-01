package com.rasel.androidbaseapp.data.source

import org.junit.Assert.*

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rasel.androidbaseapp.data.repository.LocalizationCache
import com.rasel.androidbaseapp.fakes.FakeData
import com.rasel.androidbaseapp.util.AppLanguage
import com.rasel.androidbaseapp.utils.DataBaseTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalizationCacheDataSourceTest : DataBaseTest() {

    @Mock
    lateinit var localizationCache: LocalizationCache

    private lateinit var sut: LocalizationCacheDataSource

    @Before
    fun setUp() {
        sut = LocalizationCacheDataSource(localizationCache)
    }

    @Test
    fun `get getLocalizationFromCache with appLanguage should return localizations from local cache`() =
        runTest {
            // Arrange (Given)
            val appLanguage = AppLanguage.ENGLISH
            `when`(localizationCache.getLocalization(appLanguage)) doReturn FakeData.getLocalization(
                appLanguage
            )

            // Act (When)
            val localizations = sut.getLocalizationFromCache(appLanguage)

            // Assert (Then)
            assertEquals(localizations.lblEnglish, "English")
            verify(localizationCache, times(1)).getLocalization(appLanguage)
        }

    @Test
    fun `get LocalizationFromCache with appLanguage should return error`() = runTest {
        // Arrange (Given)
        val appLanguage = AppLanguage.ENGLISH
        whenever(localizationCache.getLocalization(appLanguage)) doAnswer { throw IOException() }

        // Act (When)
        val result = kotlin.runCatching { sut.getLocalizationFromCache(appLanguage) }

        // Assert (Then)
        assertThat(result.exceptionOrNull(), instanceOf(IOException::class.java))
        verify(localizationCache, times(1)).getLocalization(appLanguage)
    }

    @Test
    fun `get AppLanguage should return AppLanguage from local cache`() =
        runTest {
            // Arrange (Given)
            val appLanguage = AppLanguage.ENGLISH
            `when`(localizationCache.getAppLanguage()) doReturn appLanguage

            // Act (When)
            val localizations = sut.getAppLanguage()

            // Assert (Then)
            assertEquals(localizations.value, "English")
            verify(localizationCache, times(1)).getAppLanguage()
        }

    @Test
    fun `get AppLanguage should return error`() = runTest {
        // Arrange (Given)
        val appLanguage = AppLanguage.ENGLISH
        whenever(localizationCache.getAppLanguage()) doAnswer { throw IOException() }

        // Act (When)
        val result = kotlin.runCatching { sut.getAppLanguage() }

        // Assert (Then)
        assertThat(result.exceptionOrNull(), instanceOf(IOException::class.java))
        verify(localizationCache, times(1)).getAppLanguage()
    }

    @Test
    fun `save AppLanguage should save AppLanguage into local cache`() =
        runTest {
            // Arrange (Given)
            val appLanguage = AppLanguage.ENGLISH
            // Act (When)
            sut.saveAppLanguage(appLanguage)

            // Assert (Then)
            verify(localizationCache, times(1)).saveAppLanguage(appLanguage)
        }

    @Test
    fun `save AppLanguage should return error failed to save last time`() =
        runTest {
            // Arrange (Given)
            val appLanguage = AppLanguage.ENGLISH
            whenever(localizationCache.saveAppLanguage(appLanguage)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.saveAppLanguage(appLanguage) }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(localizationCache, times(1)).saveAppLanguage(appLanguage)
            verify(localizationCache, times(0)).setLastCacheTime(any())
        }

    @Test
    fun `save localization with passed localization should save localization into local cache`() =
        runTest {
            // Arrange (Given)
            val localization = FakeData.getLocalization(AppLanguage.ENGLISH)
            // Act (When)
            sut.saveLocalization(localization)

            // Assert (Then)
            verify(localizationCache, times(1)).saveLocalization(localization)
            verify(localizationCache, times(1)).setLastCacheTime(any())
        }

    @Test
    fun `save localization with passed localization list should return error failed to save last time`() =
        runTest {
            // Arrange (Given)
            val localization = FakeData.getLocalization(AppLanguage.ENGLISH)

            whenever(localizationCache.saveLocalization(localization)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.saveLocalization(localization) }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(localizationCache, times(1)).saveLocalization(localization)
            verify(localizationCache, times(0)).setLastCacheTime(any())
        }

    @Test
    fun `is cached should return true`() = runTest {
        // Arrange (Given)
        `when`(localizationCache.isCached()) doReturn true

        // Act (When)
        val resultStatus = sut.isCached()

        // Assert (Then)
        assertTrue(resultStatus)
        verify(localizationCache, times(1)).isCached()
    }

    @Test
    fun `is cached should return false`() = runTest {
        // Arrange (Given)
        `when`(localizationCache.isCached()) doReturn false

        // Act (When)
        val resultStatus = sut.isCached()

        // Assert (Then)
        assertFalse(resultStatus)
        verify(localizationCache, times(1)).isCached()
    }
}