package com.example.workmaterick.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun FilterScreen(
    navController: NavController,
    viewModel: CharacterListViewModel = viewModel()
) {
    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var selectedGender by remember { mutableStateOf<String?>(null) }
    var selectedSpecies by remember { mutableStateOf<String?>(null) }

    var expandedStatus by remember { mutableStateOf(false) }
    var expandedGender by remember { mutableStateOf(false) }
    var expandedSpecies by remember { mutableStateOf(false) }

    val statuses = listOf(null, "alive", "dead", "unknown")
    val genders = listOf(null, "male", "female", "genderless", "unknown")
    val species = listOf(null, "Human", "Alien", "Humanoid", "Robot", "Animal")

    Column(Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("Выберите фильтры", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        FilterDropdown(
            "Статус",
            selectedStatus,
            statuses,
            expanded = expandedStatus,
            onExpandChange = {expandedStatus = it},
            onSelected = { selectedStatus = it }
        )

        FilterDropdown(
            "Пол",
            selectedGender,
            genders,
            expanded = expandedGender,
            onExpandChange = {expandedGender = it},
            onSelected = { selectedGender = it }
        )

        FilterDropdown(
            "Вид",
            selectedSpecies,
            species,
            expanded = expandedSpecies,
            onExpandChange = {expandedSpecies = it},
            onSelected = { selectedSpecies = it }
        )

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){

            OutlinedButton(
                onClick = {
                    selectedStatus = null
                    selectedGender = null
                    selectedSpecies = null
                }
            ) {
                Text("Сбросить")
            }

            Button(
                onClick = {
                    val route = buildString {
                        append("character_list")
                        append("?status=${selectedStatus.orEmpty()}")
                        append("&gender=${selectedGender.orEmpty()}")
                        append("&species=${selectedSpecies.orEmpty()}")
                    }
                    navController.navigate(
                        route = route
                    ) {
                        popUpTo("character_list") { inclusive = true }
                    }

//                viewModel.loadCharacters(
//                    status = selectedStatus,
//                    gender = selectedGender,
//                    species = selectedSpecies
//                )
//                navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Применить фильтр")
            }
        }


    }
}