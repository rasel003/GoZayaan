package com.rasel.androidbaseapp.domain.interactor

import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.rasel.androidbaseapp.domain.repository.CharacterRepository
import com.rasel.androidbaseapp.utils.DomainBaseTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
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
class CharacterBookmarkTestDomain : DomainBaseTest() {

    @Mock
    lateinit var characterRepository: CharacterRepository

    lateinit var sut: CharacterBookmarkUseCase

    @Before
    fun setUp() {
        sut = CharacterBookmarkUseCase(characterRepository)
    }

    @Test
    fun `set bookmark character with id should return success with response code`() = runTest {
            // Arrange (Given)
            val characterId = 1L
            whenever(characterRepository.setCharacterBookmarked(characterId)) doReturn flow { emit(1) }

            // Act (When)
            val status = sut(characterId).single()

            // Assert (Then)
            assertEquals(status, 1)
            verify(characterRepository, times(1)).setCharacterBookmarked(characterId)
        }

    @Test
    fun `set bookmark character with id should return fail with response code`() = runTest {
            // Arrange (Given)
            val characterId = 1L
            whenever(characterRepository.setCharacterBookmarked(characterId)) doReturn flow { emit(0) }

            // Act (When)
            val status = sut(characterId).single()

            // Assert (Then)
            assertEquals(status, 0)
            verify(characterRepository, times(1)).setCharacterBookmarked(characterId)
        }

    @Test
    fun `set bookmark character with id should return error result with exception`() = runTest {
            // Arrange (Given)
            val characterId = 1L
            whenever(characterRepository.setCharacterBookmarked(characterId)) doAnswer { throw IOException() }

            // Act (When)
            val result = kotlin.runCatching { sut(characterId).single() }

            // Assert (Then)
            assertThat(
                result.exceptionOrNull(),
                instanceOf(IOException::class.java)
            )
            verify(characterRepository, times(1)).setCharacterBookmarked(characterId)
        }
}
