package com.example.workmaterick.data.repository

import com.example.workmaterick.data.api.RickAndMortyApi
import com.example.workmaterick.data.local.dao.CharacterDao
import com.example.workmaterick.data.local.entity.toDomain
import com.example.workmaterick.data.model.toDomain
import com.example.workmaterick.data.model.toEntity
import com.example.workmaterick.domain.model.Character
import com.example.workmaterick.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val api: RickAndMortyApi,
    private val dao: CharacterDao
) : CharacterRepository {

    override fun getCharacters(
        name: String?,
        status: String?,
        species: String?,
        gender: String?
    ): Flow<List<Character>> = flow {
        try {
            val response = api.getAllCharacters(
                name = name,
                status = status,
                species = species,
                gender = gender
            )
            val entities = response.results.map { it.toEntity() }

            // Обновляем базу
            dao.clearAll()
            dao.insertAll(entities)

            emit(entities.map { it.toDomain() })
        } catch (e: Exception) {
            // Если ошибка — отдаём данные из базы
            emitAll(
                dao.getAllCharacters().map { list -> list.map { it.toDomain() } }
            )
        }
    }
}