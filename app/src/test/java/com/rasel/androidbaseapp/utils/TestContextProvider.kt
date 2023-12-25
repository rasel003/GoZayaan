package com.rasel.androidbaseapp.utils

import com.rasel.androidbaseapp.util.CoroutineContextProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@ExperimentalCoroutinesApi
class TestContextProvider : CoroutineContextProvider {
    private val test = UnconfinedTestDispatcher()

    override val io: CoroutineDispatcher = test

    override val default: CoroutineDispatcher = test

    override val main: CoroutineDispatcher = test
}
