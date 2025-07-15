package com.example.workmaterick.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workmaterick.domain.model.Character

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)

fun CharacterEntity.toDomain() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    gender = gender,
    image = image
)