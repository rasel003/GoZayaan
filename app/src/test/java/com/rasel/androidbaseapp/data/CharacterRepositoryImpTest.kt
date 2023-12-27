package com.rasel.androidbaseapp.data

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rasel.androidbaseapp.data.mapper.CharacterMapper
import com.rasel.androidbaseapp.data.models.CharacterEntity
import com.rasel.androidbaseapp.data.repository.CharacterDataSource
import com.rasel.androidbaseapp.data.source.CharacterDataSourceFactory
import com.rasel.androidbaseapp.fakes.FakeCharacters
import com.rasel.androidbaseapp.utils.DataBaseTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.single
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
class CharacterRepositoryImpTest : DataBaseTest() {

    @Mock
    lateinit var dataSourceFactory: CharacterDataSourceFactory

    @Mock
    lateinit var characterMapper: CharacterMapper

    @Mock
    lateinit var dataSource: CharacterDataSource

    lateinit var sut: CharacterRepositoryImp

    @Before
    fun setUp() {
        sut = CharacterRepositoryImp(dataSourceFactory, characterMapper)
    }

    @Test
    fun `get characters with cached true should return character list from local cache`() =
        runTest {
            // Arrange (Given)
            val isCached = true
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.isCached()) doReturn isCached
            `when`(dataSourceFactory.getDataStore(isCached)) doReturn dataSource
            `when`(
                dataSourceFactory.getDataStore(isCached).getCharacters()
            ) doReturn FakeCharacters.getCharacters()

            // Act (When)
            val characters = sut.getCharacters().single()

            // Assert (Then)
            assertEquals(characters.size, 2)
            verify(dataSourceFactory, times(2)).getCacheDataSource()
            verify(dataSource, times(1)).isCached()
            verify(dataSourceFactory, times(2)).getDataStore(isCached)
            verify(dataSourceFactory.getDataStore(isCached), times(1)).getCharacters()
            verify(characterMapper, times(2)).mapFromEntity(any())
        }

    @Test
    fun `get characters with cached true should return character list from local cache and saved the characters to local db`() =
        runTest {
            // Arrange (Given)
            val isCached = true
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.isCached()) doReturn isCached
            `when`(dataSourceFactory.getDataStore(isCached)) doReturn dataSource
            `when`(
                dataSourceFactory.getDataStore(isCached).getCharacters()
            ) doReturn FakeCharacters.getCharacters()

            // Act (When)
            val characters = sut.getCharacters().single()

            // Assert (Then)
            assertEquals(characters.size, 2)
            verify(dataSourceFactory, times(2)).getCacheDataSource()
            verify(dataSource, times(1)).isCached()
            verify(dataSourceFactory, times(2)).getDataStore(isCached)
            verify(dataSourceFactory.getDataStore(isCached), times(1)).getCharacters()
            verify(characterMapper, times(2)).mapFromEntity(any())
            verify(characterMapper, times(2)).mapToEntity(anyOrNull())
            verify(dataSourceFactory.getCacheDataSource(), times(1)).saveCharacters(any())
        }

    @Test
    fun `get characters with cached false should return character list from remote`() =
        runTest {
            // Arrange (Given)
            val isCached = false
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.isCached()) doReturn isCached
            `when`(dataSourceFactory.getDataStore(isCached)) doReturn dataSource
            `when`(
                dataSourceFactory.getDataStore(isCached).getCharacters()
            ) doReturn FakeCharacters.getCharacters()

            // Act (When)
            val characters = sut.getCharacters().single()

            // Assert (Then)
            assertEquals(characters.size, 2)
            verify(dataSourceFactory, times(2)).getCacheDataSource()
            verify(dataSource, times(1)).isCached()
            verify(dataSourceFactory, times(2)).getDataStore(isCached)
            verify(dataSourceFactory.getDataStore(isCached), times(1)).getCharacters()
            verify(characterMapper, times(2)).mapFromEntity(any())
        }

    @Test
    fun `get character with id should return character from local cache`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.getCharacter(characterId)) doReturn FakeCharacters.getCharacters()[0]
            `when`(characterMapper.mapFromEntity(any())) doReturn FakeCharacters.getCharacter()

            // Act (When)
            val character = sut.getCharacter(characterId).single()

            // Assert (Then)
            assertEquals(character.id, 1)
            assertEquals(character.name, "Rick")
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSourceFactory.getCacheDataSource(), times(1)).getCharacter(characterId)
            verify(characterMapper, times(1)).mapFromEntity(any())
        }

    @Test
    fun `get character with id should return character from remote`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            val mockCharacter = mock<CharacterEntity> { CharacterEntity::class.java }
            `when`(mockCharacter.image) doReturn ""
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.getCharacter(characterId)) doReturn mockCharacter
            `when`(dataSourceFactory.getRemoteDataSource()) doReturn dataSource

            // Act (When)
            val character = sut.getCharacter(characterId).single()

            // Assert (Then)
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSourceFactory.getCacheDataSource(), times(2)).getCharacter(characterId)
            verify(dataSourceFactory, times(1)).getRemoteDataSource()
            verify(dataSourceFactory.getRemoteDataSource(), times(2)).getCharacter(characterId)
            verify(characterMapper, times(1)).mapFromEntity(any())
        }

    @Test
    fun `get bookmark characters should return character from local cache`() =
        runTest {
            // Arrange (Given)
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.getBookMarkedCharacters()) doReturn FakeCharacters.getCharacters()

            // Act (When)
            val characters = sut.getBookMarkedCharacters().single()

            // Assert (Then)
            assertEquals(characters.size, 2)
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSource, times(1)).getBookMarkedCharacters()
            verify(characterMapper, times(2)).mapFromEntity(any())
        }

    @Test
    fun `get bookmark characters should return error`() =
        runTest {
            // Arrange (Given)
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            whenever(dataSource.getBookMarkedCharacters()) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.getBookMarkedCharacters().single() }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSource, times(1)).getBookMarkedCharacters()
        }

    @Test
    fun `set bookmark character should return bookmark status fail`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            val mockResult = 0 // Fail result
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.setCharacterBookmarked(characterId)) doReturn mockResult

            // Act (When)
            val bookmarkStatus = sut.setCharacterBookmarked(characterId).single()

            // Assert (Then)
            assertEquals(bookmarkStatus, mockResult)
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSource, times(1)).setCharacterBookmarked(characterId)
        }

    @Test
    fun `set bookmark character should return bookmark status success`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            val mockResult = 1
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.setCharacterBookmarked(characterId)) doReturn mockResult

            // Act (When)
            val bookmarkStatus = sut.setCharacterBookmarked(characterId).single()

            // Assert (Then)
            assertEquals(bookmarkStatus, mockResult)
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSource, times(1)).setCharacterBookmarked(characterId)
        }

    @Test
    fun `set bookmark character should return error`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            val mockResult = 1
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            whenever(dataSource.setCharacterBookmarked(characterId)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.setCharacterBookmarked(characterId).single() }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSource, times(1)).setCharacterBookmarked(characterId)
        }

    @Test
    fun `set un-bookmark character should return bookmark status fail`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            val mockResult = 0 // Fail result
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.setCharacterUnBookMarked(characterId)) doReturn mockResult

            // Act (When)
            val bookmarkStatus = sut.setCharacterUnBookMarked(characterId).single()

            // Assert (Then)
            assertEquals(bookmarkStatus, mockResult)
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSource, times(1)).setCharacterUnBookMarked(characterId)
        }

    @Test
    fun `set un-bookmark character should return bookmark status success`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            val mockResult = 1
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            `when`(dataSource.setCharacterUnBookMarked(characterId)) doReturn mockResult

            // Act (When)
            val bookmarkStatus = sut.setCharacterUnBookMarked(characterId).single()

            // Assert (Then)
            assertEquals(bookmarkStatus, mockResult)
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSource, times(1)).setCharacterUnBookMarked(characterId)
        }

    @Test
    fun `set un-bookmark character should return error`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            val mockResult = 1
            `when`(dataSourceFactory.getCacheDataSource()) doReturn dataSource
            whenever(dataSource.setCharacterUnBookMarked(characterId)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.setCharacterUnBookMarked(characterId).single() }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(dataSourceFactory, times(1)).getCacheDataSource()
            verify(dataSource, times(1)).setCharacterUnBookMarked(characterId)
        }
}
