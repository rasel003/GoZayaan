package com.rasel.androidbaseapp.data.source


import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.rasel.androidbaseapp.data.repository.LocalizationCache
import com.rasel.androidbaseapp.utils.DataBaseTest
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
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LocalizationDataSourceFactoryTest : DataBaseTest() {

    @Mock
    lateinit var localizationCache: LocalizationCache

    @Mock
    lateinit var dataSourceCache: LocalizationCacheDataSource

    @Mock
    lateinit var dataSourceRemote: LocalizationRemoteDataSource

    lateinit var sut: LocalizationDataSourceFactory

    @Before
    fun setUp() {
        sut = LocalizationDataSourceFactory(localizationCache, dataSourceCache, dataSourceRemote)
    }

    @Test
    fun `get data store with cache should return localizations from cache data-source`() = runTest {
        // Arrange (Given)
        val isCached = true
        `when`(localizationCache.isExpired()) doReturn false
        // Act (When)
        val dataSource = sut.getDataStore(isCached)
        // Assert (Then)
        assertThat(dataSource, instanceOf(LocalizationCacheDataSource::class.java))
        verify(localizationCache, times(1)).isExpired()
    }

    @Test
    fun `get data store with cache should return localizations from remote data-source`() = runTest {
        // Arrange (Given)
        val isCached = true
        `when`(localizationCache.isExpired()) doReturn true
        // Act (When)
        val dataSource = sut.getDataStore(isCached)
        // Assert (Then)
        assertThat(dataSource, instanceOf(LocalizationRemoteDataSource::class.java))
        verify(localizationCache, times(1)).isExpired()
    }
}
