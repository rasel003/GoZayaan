package com.rasel.androidbaseapp.remote.mappers

import com.rasel.androidbaseapp.data.models.CharacterLocationEntity
import com.rasel.androidbaseapp.fakes.FakeRemoteData
import com.rasel.androidbaseapp.utils.RemoteBaseTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CharacterLocationEntityMapperTest : RemoteBaseTest() {

    lateinit var sut: CharacterLocationEntityMapper

    @Before
    fun setUp() {
        sut = CharacterLocationEntityMapper()
    }

    @Test
    fun `map model to entity should return converted object`() = runTest {
            // Arrange (Given)
            val locationModel = FakeRemoteData.getCharacterModel(false).characterLocation

            // Act (When)
            val locationEntity = sut.mapFromModel(locationModel)

            // Assert (Then)
            assertThat(locationEntity, instanceOf(CharacterLocationEntity::class.java))
        }
}
