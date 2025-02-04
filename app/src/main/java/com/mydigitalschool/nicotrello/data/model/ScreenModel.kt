package com.mydigitalschool.nicotrello.data.model

import com.mydigitalschool.nicotrello.R

sealed class ScreenModel(val route: String) {
	data object Home : ScreenModel("home")
	data object Login : ScreenModel("login")
	data object Signup : ScreenModel("signup")
}

sealed class ScreenUserModel(val route: String, val label: String, val icon: Int) {
	data object Projects : ScreenUserModel("projects", "Mes projets", R.drawable.layers)
	data object AddProject : ScreenUserModel("addproject", "Ajouter un projet", R.drawable.add)
	data object Profile : ScreenUserModel( "Profile", "Mon profil", R.drawable.account_circle)
}