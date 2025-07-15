package com.example.workmaterick.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.workmaterick.data.local.dao.CharacterDao
import com.example.workmaterick.data.local.entity.CharacterEntity

@Database(entities = [CharacterEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}