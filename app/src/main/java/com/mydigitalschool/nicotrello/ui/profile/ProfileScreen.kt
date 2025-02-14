package com.mydigitalschool.nicotrello.ui.profile

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
import com.mydigitalschool.nicotrello.core.ui.components.Avatar
import com.mydigitalschool.nicotrello.core.ui.components.Loader
import com.mydigitalschool.nicotrello.core.utils.cap
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel

@Composable
fun ProfileScreen(onSetTitle: () -> Unit) {
	val authViewModel: AuthViewModel = viewModel()
	val userUID = authViewModel.getUserUID()
	val user by authViewModel.user.collectAsState()

	LaunchedEffect(Unit) {
		onSetTitle()
	}

	LaunchedEffect (Unit) {
		if (userUID != null) {
			authViewModel.getUserById(userUID)
		}
	}

	if (user == null) {
		Loader(false)
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