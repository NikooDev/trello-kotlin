package com.mydigitalschool.nicotrello.ui.screens.user.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.Timestamp
import com.mydigitalschool.nicotrello.data.model.ProjectModel
import com.mydigitalschool.nicotrello.manager.AuthManager
import com.mydigitalschool.nicotrello.utils.cap
import com.mydigitalschool.nicotrello.viewmodel.ProjectViewModel
import com.mydigitalschool.nicotrello.viewmodel.TitleViewModel
import com.mydigitalschool.nicotrello.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddProjectScreen(navController: NavController, titleViewModel: TitleViewModel, snackbarHostState: SnackbarHostState, coroutineScope: CoroutineScope) {
	val projectViewModel: ProjectViewModel = viewModel()
	val userViewModel: UserViewModel = viewModel()
	var projectName by remember { mutableStateOf("") }

	val user by userViewModel.user.collectAsState()

	val authManager = AuthManager()
	val userUID = authManager.getCurrentUser()?.uid

	LaunchedEffect(Unit) {
		titleViewModel.setTitle("Ajouter un projet")
	}

	LaunchedEffect(userUID) {
		userUID?.let { userViewModel.loadUser(it) }
	}

	Column (
		modifier = Modifier.fillMaxSize().padding(20.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center
	) {
		TextField(
			modifier = Modifier.fillMaxWidth().height(65.dp).shadow(elevation = 8.dp, shape = RoundedCornerShape(15.dp)),
			value = projectName,
			shape = RoundedCornerShape(15.dp),
			onValueChange = { projectName = it },
			label = {
				Text("Nom du projet", fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp, bottom = 2.dp))
			},
			singleLine = true,
			colors = TextFieldDefaults.colors(
				focusedIndicatorColor = MaterialTheme.colorScheme.onSecondaryContainer,
				unfocusedIndicatorColor = Color.Gray,
				unfocusedContainerColor = Color.White,
				focusedContainerColor = Color.White,
				disabledTextColor = Color.Gray,
				focusedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
				unfocusedLabelColor = Color.Gray
			),
			textStyle = TextStyle(
				fontSize = 20.sp
			)
		)

		Spacer(modifier = Modifier.height(16.dp))

		Row (
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.End
		) {
			Button (
				modifier = Modifier.height(45.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
					contentColor = Color.White
				),
				enabled = projectName.isNotEmpty(),
				onClick = {
					if (projectName.isNotEmpty()) {
						val newProject = user?.let {
							val timestamp = Timestamp.now()

							ProjectModel.from(
								title = projectName,
								author = "${it.firstname.cap()} ${it.lastname.cap()}",
								userUID = it.uid,
								nbTasks = 0,
								nbTasksEnd = 0,
								created = timestamp
							)
						}
						if (newProject != null) {
							projectViewModel.addProject(newProject) { success ->
								if (success) {
									projectName = ""
									coroutineScope.launch {
										snackbarHostState.showSnackbar(
											message = "Votre projet a bien été créé !",
											duration = SnackbarDuration.Short
										)
									}
								} else {
									coroutineScope.launch {
										snackbarHostState.showSnackbar(
											message = "Erreur lors de la création du projet",
											duration = SnackbarDuration.Short
										)
									}
								}
							}
						}
					}
				}
			) {
				Text(
					modifier = Modifier.padding(top = 2.dp, bottom = 5.dp),
					text = "Suivant",
					fontSize = 20.sp
				)
			}
		}
	}
}