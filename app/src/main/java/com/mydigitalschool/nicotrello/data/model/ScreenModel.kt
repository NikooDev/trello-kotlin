package com.mydigitalschool.nicotrello.data.model

sealed class ScreenModel(val route: String) {
	data object Home : ScreenModel("home")
	data object Login : ScreenModel("login")
	data object Signup : ScreenModel("signup")
}

sealed class ScreenUserModel(val route: String) {
	data object Projects : ScreenUserModel("projects")
	data object AddProject : ScreenUserModel("addproject")
	data object List : ScreenUserModel("list")
	data object Task : ScreenUserModel("Task")
	data object Profile : ScreenUserModel("profile")
}

const val Guest = "guest"
const val Start = "start"
const val User = "user"