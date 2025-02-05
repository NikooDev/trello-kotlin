package com.mydigitalschool.nicotrello.ui.screens.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mydigitalschool.nicotrello.manager.AuthManager
import com.mydigitalschool.nicotrello.ui.components.Avatar
import com.mydigitalschool.nicotrello.ui.components.Loader
import com.mydigitalschool.nicotrello.utils.cap
import com.mydigitalschool.nicotrello.viewmodel.UserViewModel
import kotlinx.coroutines.delay

@Composable
fun ProfileScreen(navController: NavController) {
	val userViewModel: UserViewModel = viewModel()
	val authManager = AuthManager()

	LaunchedEffect (Unit) {
		val currentUser = authManager.getCurrentUser()
		if (currentUser != null) {
			delay(1000)
			userViewModel.observeUserChanges(currentUser.uid)
		}
	}

	val user by userViewModel.user.collectAsState()

	if (user == null) {
		Loader()
	} else {
		Column (
			modifier = Modifier.fillMaxSize()
				.background(MaterialTheme.colorScheme.surfaceDim)
				.padding(20.dp)
		) {
			user?.let {
				Row(
					verticalAlignment = Alignment.CenterVertically
				) {
					Avatar("${it.firstname} ${it.lastname}", 100.dp)
					Column(
						modifier = Modifier.padding(start = 20.dp)
					) {
						Text(
							text = "${it.firstname.cap()} ${it.lastname.cap()}",
							fontSize = 20.sp,
							fontWeight = FontWeight.SemiBold,
							overflow = TextOverflow.Ellipsis,
							maxLines = 1
						)
						Text(
							text = it.email,
							color = Color.DarkGray,
							overflow = TextOverflow.Ellipsis,
							maxLines = 1
						)
					}
				}
			}
		}
	}
}