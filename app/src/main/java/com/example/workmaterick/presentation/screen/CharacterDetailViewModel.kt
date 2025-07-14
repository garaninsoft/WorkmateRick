package com.example.workmaterick.presentation.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmaterick.data.api.RickAndMortyApi
import com.example.workmaterick.domain.model.Character
import com.example.workmaterick.data.model.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val api: RickAndMortyApi // или репозиторий, если уже есть
) : ViewModel() {

    var character by mutableStateOf<Character?>(null)
        private set

    var isLoading by mutableStateOf(true)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        val id = savedStateHandle.get<Int>("id")
        if (id != null) {
            loadCharacter(id)
        } else {
            errorMessage = "Некорректный ID персонажа"
            isLoading = false
        }
    }

    private fun loadCharacter(id: Int) {
        viewModelScope.launch {
            isLoading = true
            try {
                val dto = api.getCharacterById(id)
                character = dto.toDomain()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }
}
