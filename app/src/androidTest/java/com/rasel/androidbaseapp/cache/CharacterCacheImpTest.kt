package com.rasel.androidbaseapp.cache

import androidx.test.runner.AndroidJUnit4
import com.rasel.androidbaseapp.cache.mapper.CharacterCacheMapper
import com.rasel.androidbaseapp.cache.mapper.CharacterLocationCacheMapper
import com.rasel.androidbaseapp.cache.preferences.PreferenceProvider
import com.rasel.androidbaseapp.fakes.FakeCacheData
import com.rasel.androidbaseapp.utils.CacheBaseTest
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@Config(manifest = Config.NONE)
@RunWith(AndroidJUnit4::class)
class CharacterCacheImpTest : CacheBaseTest() {

    private val locationMapper = CharacterLocationCacheMapper()
    private val characterCacheMapper = CharacterCacheMapper(locationMapper)
    private lateinit var preferencesHelper: PreferenceProvider
    private lateinit var sut: CharacterCacheImp

    @Before
    override fun setup() {
        super.setup()
        preferencesHelper = PreferenceProvider(context)
        sut = CharacterCacheImp(charaterDao, characterCacheMapper, preferencesHelper)
    }

    @Test
    fun getCharactersShouldReturnSuccessCharactersFromLocalRoomCache() = runTest {
        // Arrange (Given)
        val characterEntity = FakeCacheData.getFakeCharacterEntity(7)
        // Saving characters to database so we can get it when select query executes
        sut.saveCharacters(characterEntity)

        // Act (When)
        val characters = sut.getCharacters()

        // Assert (Then)
        assertEquals(characters.size, 7)
    }

    @Test
    fun getCharactersShouldReturnSuccessCharactersFromLocalRoomCacheWithEmptyList() = runTest {
            // Arrange (Given) no arrange

            // Act (When)
            val characters = sut.getCharacters()

            // Assert (Then)
            assertTrue(characters.isEmpty())
        }

    @Test
    fun getCharacterWithSpecificIDShouldReturnSuccessCharacterFromLocalRoomCache() = runTest {
            // Arrange (Given)
            val characterId = 1L
            val characterEntity = FakeCacheData.getFakeCharacterEntity(1, false)
            // Saving characters to database so we can get it when select query executes
            sut.saveCharacters(characterEntity)

            // Act (When)
            val character = sut.getCharacter(characterId)

            // Assert (Then)
            assertNotNull(character)
            assertEquals(character.id, 1)
        }

    @Test
    fun saveCharacterShouldReturnSavedCharactersFromLocalRoomCache() = runTest {
            // Arrange (Given)
            val characterEntity = FakeCacheData.getFakeCharacterEntity(7)
            // Act (When)
            sut.saveCharacters(characterEntity)
            val characters = sut.getCharacters()
            // Assert (Then)
            assertEquals(characters.size, 7)
        }

    @Test
    fun getBookmarkCharactersShouldReturnSuccessBookmarkedCharactersFromLocalRoomCache() = runTest {
            // Arrange (Given)
            // Saving characters to database so we can get it when select query executes
            val characterEntity =
                FakeCacheData.getFakeCharacterEntity(size = 7, isBookmarked = true)
            sut.saveCharacters(characterEntity)

            // Act (When)
            val bookmarkCharacters = sut.getBookMarkedCharacters()

            // Assert (Then)
            assertEquals(bookmarkCharacters.size, 7)
        }

    @Test
    fun getBookmarkCharactersShouldReturnSuccessBookmarkedCharactersFromLocalRoomCacheWithEmptyList() = runTest {
            // Arrange (Given) no arrange

            // Act (When)
            val characters = sut.getBookMarkedCharacters()

            // Assert (Then)
            assertTrue(characters.isEmpty())
        }

    @Test
    fun setBookmarkWithSpecificIDShouldReturnSuccessStatusFromLocalRoomCache() = runTest {
            // Arrange (Given)
            val characterId = 1L
            // Saving characters to database so we can get it when select query executes
            val characterEntity = FakeCacheData.getFakeCharacterEntity(1, false)
            sut.saveCharacters(characterEntity)

            // Act (When)
            val bookmarkStatus = sut.setCharacterBookmarked(characterId)

            // Assert (Then)
            assertEquals(bookmarkStatus, 1)
        }

    @Test
    fun setBookmarkWithSpecificIDShouldReturnFailStatusFromLocalRoomCache() = runTest {
            // Arrange (Given)
            val characterId = 1L

            // Act (When)
            val bookmarkStatus = sut.setCharacterBookmarked(characterId)

            // Assert (Then)
            assertEquals(bookmarkStatus, 0)
        }

    @Test
    fun setUnBookmarkWithSpecificIDShouldReturnSuccessStatusFromLocalRoomCache() = runTest {
            // Arrange (Given)
            val characterId = 1L
            // Saving characters to database so we can get it when select query executes
            val characterEntity = FakeCacheData.getFakeCharacterEntity(1, false)
            sut.saveCharacters(characterEntity)

            // Act (When)
            val bookmarkStatus = sut.setCharacterUnBookMarked(characterId)

            // Assert (Then)
            assertEquals(bookmarkStatus, 1)
        }

    @Test
    fun setUnBookmarkWithSpecificIdShouldReturnFailStatusFromLocalRoomCache() = runTest {
            // Arrange (Given)
            val characterId = 1L

            // Act (When)
            val bookmarkStatus = sut.setCharacterUnBookMarked(characterId)

            // Assert (Then)
            assertEquals(bookmarkStatus, 0)
        }

    @Test
    fun isCachedShouldReturnSuccessTrue() = runTest {
            // Arrange (Given)
            // Saving characters to database so we can get it when select query executes
            val characterEntity = FakeCacheData.getFakeCharacterEntity(1, false)
            sut.saveCharacters(characterEntity)

            // Act (When)
            val isCached = sut.isCached()

            // Assert (Then)
            assertTrue(isCached)
        }

    @Test
    fun isCachedShouldReturnSuccessFalse() = runTest {
            // Arrange (Given)
            // Act (When)
            val isCached = sut.isCached()

            // Assert (Then)
            assertFalse(isCached)
        }

    @Test
    fun setLastCachedTimeShouldReturnSavedTime() = runTest {
            // Arrange (Given)
            val time = 1000L
            // Act (When)
            sut.setLastCacheTime(time)
            val lastCacheTime = preferencesHelper.lastCacheTime

            // Assert (Then)
            assertEquals(lastCacheTime, lastCacheTime)
        }

    @Test
    fun isExpiredCacheTimeCachedTrue() = runTest {
            // Arrange (Given)
            // Act (When)
            val isExpired = sut.isExpired()

            // Assert (Then)
            assertTrue(isExpired)
        }

    @Test
    fun isExpiredCacheTimeCachedFalse() = runTest {
            // Arrange (Given)
            val time = System.currentTimeMillis()
            sut.setLastCacheTime(time)
            // Act (When)

            val isExpired = sut.isExpired()

            // Assert (Then)
            assertFalse(isExpired)
        }
}