package com.rasel.androidbaseapp.remote.mappers

interface EntityMapper<M, E> {

    fun mapFromModel(model: M): E
}
