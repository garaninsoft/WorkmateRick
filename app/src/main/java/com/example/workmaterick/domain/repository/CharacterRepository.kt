package com.example.workmaterick.domain.repository

import com.example.workmaterick.domain.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharacters(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null,
        page: Int = 1
    ): Flow<List<Character>>
}