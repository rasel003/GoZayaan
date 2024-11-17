package com.rasel.gozayaan.remote.mappers

interface EntityMapper<M, E> {

    fun mapFromModel(model: M): E
}
