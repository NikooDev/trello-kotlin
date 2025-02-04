package com.mydigitalschool.nicotrello.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.mydigitalschool.nicotrello.data.model.AuthStatus
import com.mydigitalschool.nicotrello.manager.AuthManager
import com.mydigitalschool.nicotrello.viewmodel.AuthViewModel
import com.mydigitalschool.nicotrello.viewmodel.AuthViewModelFactory
import kotlinx.coroutines.delay

@Composable
fun AppNav() {
	val authManager = AuthManager()
	val factory = AuthViewModelFactory(authManager)
	val authViewModel: AuthViewModel = viewModel(factory = factory)
	val authStatus by authViewModel.authStatus.observeAsState(AuthStatus.Undefined)
	val navController = rememberNavController()

	val gradient = Brush.linearGradient(
		colors = listOf(Color(0xFFAA17CB), Color(0xFF1D5CBE))
	)

	var isLoading by remember { mutableStateOf(true) }

	LaunchedEffect(authStatus) {
		if (authStatus is AuthStatus.Undefined) {
			delay(550)
			isLoading = false
		}
	}

	when (authStatus) {
		is AuthStatus.Undefined -> {
			AnimatedVisibility (
				visible = isLoading,
				enter = fadeIn(tween(500)),
				exit = fadeOut(tween(500))
			) {
				Box(
					modifier = Modifier.fillMaxSize().background(gradient),
					contentAlignment = Alignment.Center
				) {
					CircularProgressIndicator(color = Color.White, modifier = Modifier.size(60.dp))
				}
			}
		}
		is AuthStatus.LoggedIn -> {
			NavUser(navController, "projects")
		}
		is AuthStatus.LoggedOut -> {
			NavGuest(navController, "home")
		}
	}
}