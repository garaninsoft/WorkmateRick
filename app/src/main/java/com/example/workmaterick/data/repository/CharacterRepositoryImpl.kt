package com.example.workmaterick.data.repository

import com.example.workmaterick.data.api.RickAndMortyApi
import com.example.workmaterick.data.local.dao.CharacterDao
import com.example.workmaterick.data.local.entity.toDomain
import com.example.workmaterick.data.model.toEntity
import com.example.workmaterick.domain.model.Character
import com.example.workmaterick.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao
) : CharacterRepository {

    override fun getCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?,
        page: Int
    ): Flow<List<Character>> = flow {
        try {
            val response = api.getAllCharacters(
                page = page,
                name = name,
                status = status,
                species = species,
                gender = gender
            )

            val entities = response.results.map { it.toEntity() }

            if (page == 1) {
                dao.clearAll()
            }
            dao.insertAll(entities)

            emit(entities.map { it.toDomain() })
        } catch (_: Exception) {
            if (page == 1) {
                emitAll(dao.getAllCharacters().map { it.map { it.toDomain() } })
            } else {
                emit(emptyList()) // для page > 1 при оффлайне
            }
        }
    }
}