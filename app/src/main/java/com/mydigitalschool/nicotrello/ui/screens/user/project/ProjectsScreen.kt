package com.mydigitalschool.nicotrello.ui.screens.user.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.mydigitalschool.nicotrello.data.model.ScreenUserModel
import com.mydigitalschool.nicotrello.ui.components.Loader
import com.mydigitalschool.nicotrello.ui.components.project.ProjectCard
import com.mydigitalschool.nicotrello.viewmodel.ProjectViewModel
import kotlinx.coroutines.delay

@Composable
fun ProjectsScreen(navController: NavController) {
	val projectViewModel: ProjectViewModel = viewModel()
	var isLoading by remember { mutableStateOf(true) }

	LaunchedEffect (Unit) {
		delay(500)
		projectViewModel.fetchProjects()
		isLoading = false
	}

	val projects by projectViewModel.projects.collectAsState()

	Column (
		modifier = Modifier.fillMaxSize(),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		if (isLoading) {
			Loader()
		} else {
			if (projects.isNotEmpty()) {
				LazyColumn (
					modifier = Modifier.fillMaxSize().padding(vertical = 10.dp, horizontal = 7.dp)
				) {
					items(projects) { project ->
						ProjectCard(project) {

						}
					}
				}
			} else {
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					Column(horizontalAlignment = Alignment.CenterHorizontally) {
						Text(
							text = "Aucun projet disponible",
							style = MaterialTheme.typography.bodyLarge,
							fontSize = 25.sp
						)
						Spacer(modifier = Modifier.height(15.dp))
						Button(
							modifier = Modifier.height(45.dp),
							onClick = {
								navController.navigate(ScreenUserModel.AddProject.route)
							},
							colors = ButtonDefaults.buttonColors(
								containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
								contentColor = Color.White
							)
						) {
							Text(
								modifier = Modifier.padding(top = 2.dp, bottom = 5.dp),
								text = "Créer un projet",
								fontSize = 20.sp
							)
						}
					}
				}
			}
		}
	}
}