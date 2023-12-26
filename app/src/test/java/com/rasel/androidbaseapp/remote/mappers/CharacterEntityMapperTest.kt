package com.rasel.androidbaseapp.remote.mappers

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.verify
import com.rasel.androidbaseapp.data.models.CharacterEntity
import com.rasel.androidbaseapp.fakes.FakeRemoteData
import com.rasel.androidbaseapp.utils.RemoteBaseTest
import junit.framework.TestCase.assertEquals
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

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharacterEntityMapperTest : RemoteBaseTest() {

    @Mock
    lateinit var mapper: CharacterLocationEntityMapper

    lateinit var sut: CharacterEntityMapper

    @Before
    fun setUp() {
        sut = CharacterEntityMapper(mapper)
    }

    @Test
    fun `map model to entity should return converted object`() = runTest {
            // Arrange (Given)
            val characterModel = FakeRemoteData.getCharacterModel(false)
            `when`(mapper.mapFromModel(characterModel.characterLocation)) doReturn FakeRemoteData.getCharacterEntity(
                false
            ).characterLocation
            // Act (When)
            val character = sut.mapFromModel(characterModel)

            // Assert (Then)
            assertThat(character, instanceOf(CharacterEntity::class.java))
            assertEquals(character.id, 1)
            assertTrue(character.name.isNotEmpty())
            verify(mapper).mapFromModel(any())
        }
}
