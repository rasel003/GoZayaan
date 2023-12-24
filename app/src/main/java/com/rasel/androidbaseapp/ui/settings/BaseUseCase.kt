package com.rasel.androidbaseapp.ui.settings

interface BaseUseCase<in Parameter, out Result> {
    suspend operator fun invoke(params: Parameter): Result
}
