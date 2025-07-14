package com.example.workmaterick.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun CharacterListScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val isRefreshing = state is UiState.Loading

    var query by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.loadCharacters(name = query)
            },
            label = { Text("Поиск по имени") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
//
//        // ========== FILTER: STATUS ==========
//        var expandedStatus by remember { mutableStateOf(false) }
//        val statusOptions = listOf(null, "alive", "dead", "unknown")
//        var selectedStatus by remember { mutableStateOf<String?>(null) }
//
//        var selectedSpecies by remember { mutableStateOf<String?>(null) }
//        var selectedGender by remember { mutableStateOf<String?>(null) }
//
//        FilterDropdown(
//            label = "Статус",
//            selected = selectedStatus,
//            options = statusOptions,
//            expanded = expandedStatus,
//            onExpandChange = { expandedStatus = it },
//            onSelected = {
//                selectedStatus = it
//                viewModel.loadCharacters(
//                    name = query,
//                    status = it,
//                    gender = selectedGender,
//                    species = selectedSpecies
//                )
//            }
//        )
//
//// ========== FILTER: GENDER ==========
//        var expandedGender by remember { mutableStateOf(false) }
//        val genderOptions = listOf(null, "male", "female", "genderless", "unknown")
//
//        FilterDropdown(
//            label = "Пол",
//            selected = selectedGender,
//            options = genderOptions,
//            expanded = expandedGender,
//            onExpandChange = { expandedGender = it },
//            onSelected = {
//                selectedGender = it
//                viewModel.loadCharacters(
//                    name = query,
//                    status = selectedStatus,
//                    gender = it,
//                    species = selectedSpecies
//                )
//            }
//        )
//
//// ========== FILTER: SPECIES ==========
//        var expandedSpecies by remember { mutableStateOf(false) }
//        val speciesOptions = listOf(null, "Human", "Alien", "Humanoid", "Robot", "Animal")
//
//        FilterDropdown(
//            label = "Вид",
//            selected = selectedSpecies,
//            options = speciesOptions,
//            expanded = expandedSpecies,
//            onExpandChange = { expandedSpecies = it },
//            onSelected = {
//                selectedSpecies = it
//                viewModel.loadCharacters(
//                    name = query,
//                    status = selectedStatus,
//                    gender = selectedGender,
//                    species = it
//                )
//            }
//        )

//        navController.navigate(
//            "character_list" +
//                    "?status=${selectedStatus.orEmpty()}" +
//                    "&gender=${selectedGender.orEmpty()}" +
//                    "&species=${selectedSpecies.orEmpty()}"
//        ) {
//            popUpTo("character_list") { inclusive = true } // чтобы заменить экран, не возвращаться назад
//        }



        Button(
            onClick = { navController.navigate("filter_screen") },
            modifier = Modifier
                .align(Alignment.End)
                .padding(horizontal = 8.dp)
        ) {
            Text("Фильтр")
        }

        // Pull-to-Refresh обёртка
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                viewModel.loadCharacters(name = query)
            },
            modifier = Modifier.fillMaxSize()
        ){
            when (state) {
                is UiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    val message = (state as UiState.Error).message
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = message, color = Color.Red)
                    }
                }

                is UiState.Success -> {
                    val characters = (state as UiState.Success).characters

                    if (characters.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Ничего не найдено")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            contentPadding = PaddingValues(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(characters) { character ->
                                CharacterCard(character)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterDropdown(
    label: String,
    selected: String?,
    options: List<String?>,
    expanded: Boolean,
    onExpandChange: (Boolean) -> Unit,
    onSelected: (String?) -> Unit
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 4.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onExpandChange(true) }
                .padding(4.dp)
        ) {
            Text("$label: ", modifier = Modifier.padding(end = 8.dp))
            Text(selected ?: "Любой", fontWeight = FontWeight.Bold)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandChange(false) }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option ?: "Любой") },
                    onClick = {
                        onExpandChange(false)
                        onSelected(option)
                    }
                )
            }
        }
    }
}