package com.example.workmaterick.data.model

import com.example.workmaterick.domain.model.Character

data class CharacterDto(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val origin: LocationInfo,
    val location: LocationInfo,
    val image: String
)

data class LocationInfo(
    val name: String,
    val url: String
)

fun CharacterDto.toDomain(): Character {
    return Character(
        id = id,
        name = name,
        image = image,
        species = species,
        status = status,
        gender = gender
    )
}
