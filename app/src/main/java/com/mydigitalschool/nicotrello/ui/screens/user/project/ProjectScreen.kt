package com.mydigitalschool.nicotrello.ui.screens.user.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavController
import com.mydigitalschool.nicotrello.viewmodel.ProjectViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@Composable
fun ProjectScreen(navController: NavController, projectUID: String, onSetTitle: (String) -> Unit) {
	val projectViewModel: ProjectViewModel = viewModel()
	val coroutineScope = rememberCoroutineScope()
	var fetchJob: Job? = null

	LaunchedEffect(projectUID) {
		fetchJob?.cancel()
		fetchJob = coroutineScope.launch {
			try {
				projectViewModel.fetchProjectById(projectUID)
			} catch (e: Exception) {
				println("Erreur lors de la récupération du projet : ${e.message}")
			}
		}
	}

	val project by projectViewModel.project.collectAsState()

	LaunchedEffect(project) {
		project?.let { onSetTitle(it.title) }
	}

	Column (
		modifier = Modifier.fillMaxSize().padding(16.dp),
		horizontalAlignment = Alignment.Start,
		verticalArrangement = Arrangement.Top
	) {
		Button(
			modifier = Modifier.fillMaxWidth(),
			colors = ButtonDefaults.buttonColors(
				containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
				contentColor = Color.White
			),
			onClick = {

			}
		) {
			Text(
				"Ajouter une liste",
				modifier = Modifier.padding(top = 2.dp, bottom = 5.dp),
				fontSize = 20.sp
			)
		}
	}
}