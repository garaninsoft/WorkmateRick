package com.example.workmaterick.presentation.screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workmaterick.domain.model.Character
import com.example.workmaterick.domain.repository.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    object Loading : UiState()
    data class Success(val characters: List<Character>) : UiState()
    data class Error(val message: String) : UiState()
}

@HiltViewModel
class CharacterListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: CharacterRepository
//    private val api: RickAndMortyApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val defaultQuery = savedStateHandle.get<String>("query")
    private val defaultStatus = savedStateHandle.get<String>("status")
    private val defaultGender = savedStateHandle.get<String>("gender")
    private val defaultSpecies = savedStateHandle.get<String>("species")

    init {
        loadCharacters(
            name = defaultQuery,
            status = defaultStatus,
            gender = defaultGender,
            species = defaultSpecies
        )
    }

    fun loadCharacters(
        name: String? = null,
        status: String? = null,
        species: String? = null,
        gender: String? = null
    ) {
        _uiState.value = UiState.Loading
        viewModelScope.launch {
            repository.getCharacters(name, status, species, gender)
                .catch { e -> _uiState.value = UiState.Error("Ошибка: ${e.localizedMessage}") }
                .collect { characters ->
                    _uiState.value = UiState.Success(characters)
                }
        }
    }
}