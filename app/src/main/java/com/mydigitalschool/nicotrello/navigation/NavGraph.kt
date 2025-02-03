package com.mydigitalschool.nicotrello.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.mydigitalschool.nicotrello.ui.screens.guest.*

@Composable
fun AppNav(startDestination: String = Screen.Login.route) {
	val navController = rememberNavController()

	NavHost(navController = navController, startDestination = startDestination) {
		composable(Screen.Home.route) { HomeScreen(navController) }
		composable(Screen.Login.route) { LoginScreen(navController) }
		composable(Screen.Register.route) { SignupScreen(navController) }
	}
}