package com.rasel.androidbaseapp.data.source

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rasel.androidbaseapp.data.repository.CharacterCache
import com.rasel.androidbaseapp.fakes.FakeCharacters
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
class CharacterCacheDataSourceTest : DataBaseTest() {

    @Mock
    lateinit var characterCache: CharacterCache

    private lateinit var sut: CharacterCacheDataSource

    @Before
    fun setUp() {
        sut = CharacterCacheDataSource(characterCache)
    }

    @Test
    fun `get characters should return characters from local cache`() = runTest {
        // Arrange (Given)
        `when`(characterCache.getCharacters()) doReturn FakeCharacters.getCharacters()

        // Act (When)
        val characters = sut.getCharacters()

        // Assert (Then)
        assertEquals(characters.size, 2)
        verify(characterCache, times(1)).getCharacters()
    }

    @Test
    fun `get characters should return error`() =
        runTest {
            // Arrange (Given)
            whenever(characterCache.getCharacters()) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.getCharacters() }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(characterCache, times(1)).getCharacters()
        }

    @Test
    fun `get character with character-id should return characters from local cache`() = runTest {
        // Arrange (Given)
        val characterId = 1L
        `when`(characterCache.getCharacter(characterId)) doReturn FakeCharacters.getCharacters()[0]

        // Act (When)
        val characters = sut.getCharacter(characterId)

        // Assert (Then)
        assertEquals(characters.name, "Rick")
        verify(characterCache, times(1)).getCharacter(characterId)
    }

    @Test
    fun `get character with character-id should return error`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            whenever(characterCache.getCharacter(characterId)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.getCharacter(characterId) }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(characterCache, times(1)).getCharacter(characterId)
        }

    @Test
    fun `save character passed character list should save character into local cache`() = runTest {
        // Arrange (Given)
        val characters = FakeCharacters.getCharacters()
        // Act (When)
        sut.saveCharacters(characters)

        // Assert (Then)
        verify(characterCache, times(1)).saveCharacters(characters)
        verify(characterCache, times(1)).setLastCacheTime(any())
    }

    @Test
    fun `save character passed character list should return error failed to save last time`() =
        runTest {
            // Arrange (Given)
            val characters = FakeCharacters.getCharacters()
            whenever(characterCache.saveCharacters(characters)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.saveCharacters(characters) }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(characterCache, times(1)).saveCharacters(characters)
            verify(characterCache, times(0)).setLastCacheTime(any())
        }

    @Test
    fun `get bookmark characters should return characters from local cache`() = runTest {
        // Arrange (Given)
        `when`(characterCache.getBookMarkedCharacters()) doReturn FakeCharacters.getCharacters()

        // Act (When)
        val characters = sut.getBookMarkedCharacters()

        // Assert (Then)
        assertEquals(characters.size, 2)
        verify(characterCache, times(1)).getBookMarkedCharacters()
    }

    @Test
    fun `get bookmark characters should return error`() =
        runTest {
            // Arrange (Given)
            whenever(characterCache.getBookMarkedCharacters()) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.getBookMarkedCharacters() }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(characterCache, times(1)).getBookMarkedCharacters()
        }

    @Test
    fun `set bookmark characters should return success status`() = runTest {
        // Arrange (Given)
        val characterId = 1L
        val statusFake = 1
        `when`(characterCache.setCharacterBookmarked(characterId)) doReturn statusFake

        // Act (When)
        val resultStatus = sut.setCharacterBookmarked(characterId)

        // Assert (Then)
        assertEquals(resultStatus, statusFake)
        verify(characterCache, times(1)).setCharacterBookmarked(characterId)
    }

    @Test
    fun `set bookmark characters should return fail status`() = runTest {
        // Arrange (Given)
        val characterId = 1L
        val statusFake = 0
        `when`(characterCache.setCharacterBookmarked(characterId)) doReturn statusFake

        // Act (When)
        val resultStatus = sut.setCharacterBookmarked(characterId)

        // Assert (Then)
        assertEquals(resultStatus, statusFake)
        verify(characterCache, times(1)).setCharacterBookmarked(characterId)
    }

    @Test
    fun `set bookmark characters should return error`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            whenever(characterCache.setCharacterBookmarked(characterId)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.setCharacterBookmarked(characterId) }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(characterCache, times(1)).setCharacterBookmarked(characterId)
        }

    @Test
    fun `set un-bookmark characters should return success status`() = runTest {
        // Arrange (Given)
        val characterId = 1L
        val statusFake = 1
        `when`(characterCache.setCharacterUnBookMarked(characterId)) doReturn statusFake

        // Act (When)
        val resultStatus = sut.setCharacterUnBookMarked(characterId)

        // Assert (Then)
        assertEquals(resultStatus, statusFake)
        verify(characterCache, times(1)).setCharacterUnBookMarked(characterId)
    }

    @Test
    fun `set un-bookmark characters should return fail status`() = runTest {
        // Arrange (Given)
        val characterId = 1L
        val statusFake = 0
        `when`(characterCache.setCharacterUnBookMarked(characterId)) doReturn statusFake

        // Act (When)
        val resultStatus = sut.setCharacterUnBookMarked(characterId)

        // Assert (Then)
        assertEquals(resultStatus, statusFake)
        verify(characterCache, times(1)).setCharacterUnBookMarked(characterId)
    }

    @Test
    fun `set in-bookmark characters should return error`() =
        runTest {
            // Arrange (Given)
            val characterId = 1L
            whenever(characterCache.setCharacterUnBookMarked(characterId)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut.setCharacterUnBookMarked(characterId) }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(), instanceOf(IOException::class.java)
            )
            verify(characterCache, times(1)).setCharacterUnBookMarked(characterId)
        }

    @Test
    fun `is cached should return true`() = runTest {
        // Arrange (Given)
        `when`(characterCache.isCached()) doReturn true

        // Act (When)
        val resultStatus = sut.isCached()

        // Assert (Then)
        assertTrue(resultStatus)
        verify(characterCache, times(1)).isCached()
    }

    @Test
    fun `is cached should return false`() = runTest {
        // Arrange (Given)
        `when`(characterCache.isCached()) doReturn false

        // Act (When)
        val resultStatus = sut.isCached()

        // Assert (Then)
        assertFalse(resultStatus)
        verify(characterCache, times(1)).isCached()
    }
}
