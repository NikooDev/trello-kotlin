package com.mydigitalschool.nicotrello.ui.project

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mydigitalschool.nicotrello.core.ui.components.Btn
import com.mydigitalschool.nicotrello.core.ui.components.Input
import com.mydigitalschool.nicotrello.core.utils.cap
import com.mydigitalschool.nicotrello.data.model.ProjectModel
import com.mydigitalschool.nicotrello.data.model.ScreenUserModel
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddProjectScreen(navController: NavHostController, authViewModel: AuthViewModel, snackbarHostState: SnackbarHostState, colors: ColorScheme, onSetTitle: () -> Unit) {
	val projectViewModel: ProjectViewModel = viewModel()

	var isLoading by remember { mutableStateOf(false) }
	var projectName by remember { mutableStateOf("") }
	val focusProjectName = remember { FocusRequester() }

	val focusManager = LocalFocusManager.current
	val latestFocusManager by rememberUpdatedState(focusManager)

	val userUID = authViewModel.getUserUID()
	val user by authViewModel.user.collectAsState()

	val coroutineScope = rememberCoroutineScope()
	val keyboardController = LocalSoftwareKeyboardController.current

	LaunchedEffect(Unit) {
		onSetTitle()
	}

	LaunchedEffect(userUID) {
		userUID?.let { authViewModel.getUserById(it) }
	}

	Column(
		modifier = Modifier.fillMaxSize().padding(16.dp).pointerInput(Unit) {
			detectTapGestures {
				latestFocusManager.clearFocus()
			}
		},
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Input(
			value = projectName,
			placeholder = "Titre du projet",
			modifier = Modifier.focusRequester(focusProjectName),
			onValueChange = {
				projectName = it.cap()
			},
			enabled = !isLoading,
			unfocusedContainerColor = Color.White,
			focusContainerColor = Color.White,
			unfocusedLabelColor = Color.Gray,
			focusedLabelColor = Color.DarkGray,
			unfocusedTextColor = Color.DarkGray,
			focusedTextColor = Color.DarkGray,
			unfocusedBorderColor = Color.Transparent,
			focusedBorderColor = Color.Transparent
		)
		Spacer(modifier = Modifier.height(16.dp))
		Btn(
			modifier = Modifier.fillMaxWidth(),
			text = "Créer le projet",
			loading = isLoading,
			colorLoading = Color.White,
			enabled = projectName.isNotEmpty() || !isLoading,
			onClick = {
				if (projectName.isNotEmpty()) {
					isLoading = true
					keyboardController?.hide()

					val newProject = user?.let {
						ProjectModel.create(
							title = projectName,
							author = "${it.firstname.cap()} ${it.lastname.cap()}",
							userUID = it.uid,
							nbTasks = 0
						)
					}

					coroutineScope.launch {
						if (newProject != null) {
							projectViewModel.addProject(newProject) { success, projectUID, message ->
								if (success && message != null) {
									coroutineScope.launch {
										delay(500)
										snackbarHostState.showSnackbar(
											message = message,
											duration = SnackbarDuration.Short
										)

										delay(1000)
										projectName = ""
										navController.navigate("${ScreenUserModel.List.route}/${projectUID}") {
											popUpTo(ScreenUserModel.AddProject.route) {
												inclusive = true
											}
										}
									}
								}
							}
						}
					}
				}
			},
			backgroundColor = colors.primary,
			textColor = Color.White,
			textSize = 15.sp,
		)
	}
}