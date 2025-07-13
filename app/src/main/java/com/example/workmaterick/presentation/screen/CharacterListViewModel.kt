package com.example.workmaterick.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmaterick.data.model.toDomain
import com.example.workmaterick.di.NetworkModule
import com.example.workmaterick.domain.model.Character
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val characters: List<Character>) : UiState()
    data class Error(val message: String) : UiState()
}

class CharacterListViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            try {
                val response = NetworkModule.api.getAllCharacters()
                val characters = response.results.map { it.toDomain() }
                _uiState.value = UiState.Success(characters)
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Ошибка загрузки: ${e.localizedMessage}")
            }
        }
    }
}