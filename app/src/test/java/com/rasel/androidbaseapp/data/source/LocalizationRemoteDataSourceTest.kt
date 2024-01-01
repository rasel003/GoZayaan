package com.rasel.androidbaseapp.data.source


import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rasel.androidbaseapp.data.repository.LocalizationRemote
import com.rasel.androidbaseapp.fakes.FakeData
import com.rasel.androidbaseapp.util.AppLanguage
import com.rasel.androidbaseapp.utils.DataBaseTest
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalizationRemoteDataSourceTest : DataBaseTest() {

    @Mock
    lateinit var localizationRemote: LocalizationRemote

    lateinit var sut: LocalizationRemoteDataSource

    @Before
    fun setUp() {
        sut = LocalizationRemoteDataSource(localizationRemote)
    }

    @Test
    fun `get localization should return localizations from remote`() = runTest {
        val appLanguage = AppLanguage.ENGLISH
        val localization = FakeData.getLocalization(appLanguage)

        // Arrange (Given)
        Mockito.`when`(localizationRemote.getLocalization(appLanguage)) doReturn localization

        // Act (When)
        val localizations = sut.getLocalization(appLanguage)

        // Assert (Then)
        TestCase.assertEquals("English", localizations.lblEnglish)
        verify(localizationRemote, times(1)).getLocalization(appLanguage)
    }

    @Test
    fun `get localization should return error`() = runTest {
        // Arrange (Given)
        val appLanguage = AppLanguage.ENGLISH
        whenever(localizationRemote.getLocalization(appLanguage)) doAnswer { throw IOException() }

        // Act (When)
        val result = kotlin.runCatching {
            sut.getLocalization(appLanguage)
        }

        // Assert (Then)
        assertThat(result.exceptionOrNull(), instanceOf(IOException::class.java))
        verify(localizationRemote, times(1)).getLocalization(appLanguage)
    }

    @Test
    fun `is cached should return error - not supported by remote`() =
        runTest {
            // Arrange (Given) nothing to arrange

            // Act (When)
            val result = kotlin.runCatching { sut.isCached() }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(),
                instanceOf(UnsupportedOperationException::class.java)
            )
        }
}
