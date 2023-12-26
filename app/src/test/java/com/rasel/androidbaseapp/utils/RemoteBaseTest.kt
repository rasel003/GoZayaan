package com.rasel.androidbaseapp.utils

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
abstract class RemoteBaseTest {

    /**
     * A test rule to allow testing coroutines that use the main dispatcher
     */
    @get:Rule
    val testRule = CoroutineTestRule()

    val dispatcher = testRule.dispatcher
//    val exceptionHandler = TestCoroutineExceptionHandler()
}
