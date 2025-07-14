package com.example.workmaterick.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.workmaterick.presentation.screen.CharacterListScreen
import com.example.workmaterick.presentation.screen.FilterScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "character_list"
    ) {
        composable("character_list") {
            CharacterListScreen(navController = navController)
        }

        composable(
            route = "character_list?status={status}&gender={gender}&species={species}",
            arguments = listOf(
                navArgument("status") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = ""
                },
                navArgument("gender") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = ""
                },
                navArgument("species") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                    defaultValue = ""
                },
            )
        ) {
            CharacterListScreen(navController = navController)
        }
        composable("filter_screen") {
            FilterScreen(navController = navController)
        }
    }
}