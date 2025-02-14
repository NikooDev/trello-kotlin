package com.mydigitalschool.nicotrello.ui.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mydigitalschool.nicotrello.core.ui.components.Btn
import com.mydigitalschool.nicotrello.core.ui.components.Loader
import com.mydigitalschool.nicotrello.data.model.ScreenUserModel
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme
import com.mydigitalschool.nicotrello.ui.theme.Lato
import kotlinx.coroutines.launch

@Composable
fun ProjectsScreen(
	navController: NavHostController,
	snackbarHostState: SnackbarHostState,
	authViewModel: AuthViewModel,
	colors: ColorScheme,
	onSetTitle: () -> Unit
) {
	val projectViewModel: ProjectViewModel = viewModel()
	val projects by projectViewModel.projects.collectAsState()
	val error by projectViewModel.error.collectAsState()
	val isLoading by projectViewModel.isLoading.collectAsState()
	val userUID = authViewModel.getUserUID()

	val coroutineScope = rememberCoroutineScope()

	LaunchedEffect(Unit) {
		onSetTitle()
	}

	LaunchedEffect(error) {
		error?.let {
			coroutineScope.launch {
				snackbarHostState.showSnackbar(it)
			}
		}
	}

	LaunchedEffect(userUID) {
		if (userUID != null) {
			projectViewModel.getProjectsByUser(userUID)
		}
	}

	Column(
		modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).padding(top = 16.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		AnimatedVisibility(
			visible = isLoading,
			enter = fadeIn(animationSpec = tween(300)),
			exit = fadeOut(animationSpec = tween(300))
		) {
			Loader(false)
		}

		if (!isLoading && projects.isNotEmpty()) {
			LazyColumn(
				modifier = Modifier.fillMaxSize()
			) {
				items(projects, key = { it.uid.toString() }) { project ->
					ProjectCard(project, colors, projectViewModel, snackbarHostState, coroutineScope) {
						navController.navigate(ScreenUserModel.List.route + "/${project.uid}")
					}
				}

				item {
					Spacer(modifier = Modifier.height(16.dp))
				}
			}
		}

		AnimatedVisibility(
			visible = !isLoading && projects.isEmpty(),
			enter = fadeIn(animationSpec = tween(300)),
			exit = fadeOut(animationSpec = tween(300))
		) {
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
			) {
				Column(
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.Center
				) {
					Text(
						text = "Aucun projet trouvé",
						fontSize = 25.sp,
						fontFamily = Lato,
						color = colors.secondaryText
					)
					Spacer(modifier = Modifier.height(15.dp))
					Btn(
						text = "Créer un nouveau projet",
						onClick = {
							navController.navigate(ScreenUserModel.AddProject.route)
						},
						backgroundColor = colors.primary,
						textColor = Color.White,
						textSize = 15.sp,
					)
				}
			}
		}
	}
}