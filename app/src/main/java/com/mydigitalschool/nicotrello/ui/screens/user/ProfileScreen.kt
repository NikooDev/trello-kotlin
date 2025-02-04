package com.mydigitalschool.nicotrello.ui.screens.user

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mydigitalschool.nicotrello.manager.AuthManager
import com.mydigitalschool.nicotrello.ui.components.Loader
import com.mydigitalschool.nicotrello.utils.cap
import com.mydigitalschool.nicotrello.viewmodel.UserViewModel

@Composable
fun ProfileScreen(navController: NavController) {
	val userViewModel: UserViewModel = viewModel()
	val authManager = AuthManager()

	LaunchedEffect (Unit) {
		val currentUser = authManager.getCurrentUser()
		if (currentUser != null) {
			userViewModel.observeUserChanges(currentUser.uid)
		}
	}

	val user by userViewModel.user.collectAsState()

	if (user == null) {
		Loader()
	} else {
		Column (
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Text("Profil")
			user?.let {
				Text(text = "Bienvenue ${it.firstname.cap()} ${it.lastname.cap()}")
			}
		}
	}
}