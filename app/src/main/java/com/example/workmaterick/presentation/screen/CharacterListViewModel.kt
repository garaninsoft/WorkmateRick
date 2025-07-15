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
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _isAppending = MutableStateFlow(false)
    val isAppending: StateFlow<Boolean> = _isAppending

    private val defaultQuery = savedStateHandle.get<String>("query")
    private val defaultStatus = savedStateHandle.get<String>("status")
    private val defaultGender = savedStateHandle.get<String>("gender")
    private val defaultSpecies = savedStateHandle.get<String>("species")

    private var currentPage = 1
    private var isEndReached = false

    private val currentCharacters = mutableListOf<Character>()

    private var currentQuery: String? = defaultQuery
    private var currentStatus: String? = defaultStatus
    private var currentGender: String? = defaultGender
    private var currentSpecies: String? = defaultSpecies

    init {
        loadCharacters(reset = true)
    }

    fun loadCharacters(
        name: String? = currentQuery,
        status: String? = currentStatus,
        species: String? = currentSpecies,
        gender: String? = currentGender,
        reset: Boolean = false
    ) {
        if (_isAppending.value || isEndReached) return

        if (reset) {
            currentPage = 1
            currentCharacters.clear()
            isEndReached = false
            _uiState.value = UiState.Loading
        }

        currentQuery = name
        currentStatus = status
        currentGender = gender
        currentSpecies = species

        _isAppending.value = true
        viewModelScope.launch {
            repository.getCharacters(name, status, species, gender, currentPage)
                .catch { e ->
                    _uiState.value = UiState.Error("Ошибка: ${e.localizedMessage}")
                    _isAppending.value = false
                }
                .collect { newCharacters ->
                    _isAppending.value = false

                    if (newCharacters.isEmpty()) {
                        isEndReached = true
                    } else {
                        currentPage++
                        currentCharacters.addAll(newCharacters)
                        _uiState.value = UiState.Success(currentCharacters)
                    }
                }
        }
    }

    fun refresh() {
        loadCharacters(reset = true)
    }

    fun loadNextPage() {
        loadCharacters()
    }
}