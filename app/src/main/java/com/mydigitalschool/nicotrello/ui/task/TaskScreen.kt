package com.mydigitalschool.nicotrello.ui.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.mydigitalschool.nicotrello.core.ui.components.Loader
import com.mydigitalschool.nicotrello.core.utils.cap
import com.mydigitalschool.nicotrello.data.model.Priority
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme
import com.mydigitalschool.nicotrello.ui.theme.Lato
import com.mydigitalschool.nicotrello.core.ui.components.Btn
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(navController: NavHostController, taskUID: String, snackbarHostState: SnackbarHostState, colors: ColorScheme, onSetTitle: (String) -> Unit) {
	val taskViewModel: TaskViewModel = viewModel()
	val authViewModel: AuthViewModel = viewModel()

	val task by taskViewModel.task.collectAsState()
	val coroutineScope = rememberCoroutineScope()
	var deleteLoading by remember { mutableStateOf(false) }
	var showUpdateTask by remember { mutableStateOf(false) }
	var isLoadingUpdateTask by remember { mutableStateOf(false) }
	val userUID = authViewModel.getUserUID()
	val user by authViewModel.user.collectAsState()

	val sheetState = rememberModalBottomSheetState(
		skipPartiallyExpanded = true
	)

	LaunchedEffect(taskUID) {
		coroutineScope.launch {
			taskViewModel.getTaskById(taskUID)
		}
	}

	LaunchedEffect(task) {
		task?.let { onSetTitle(it.title) }
	}

	LaunchedEffect(Unit) {
		userUID?.let { authViewModel.getUserById(it) }
	}

	@Composable
	fun ButtonPriority(priority: Priority) {
		var text = ""
		val color: Color

		when (priority) {
			Priority.HIGH -> {
				color = colors.priorityHigh
				text = "Haute"
			}
			Priority.MEDIUM -> {
				color = colors.priorityMedium
				text = "Moyenne"
			}
			Priority.LOW -> {
				color = colors.priorityLow
				text = "Basse"
			}
		}

		return Button(
			modifier = Modifier.height(30.dp),
			onClick = {},
			colors = ButtonDefaults.buttonColors(
				containerColor = color
			),
			contentPadding = PaddingValues(vertical = 3.dp, horizontal = 14.dp)
		) {
			Text(
				text = "Priorité : ${text.uppercase()}",
				fontSize = 12.sp,
				color = Color.White
			)
		}
	}

	if (task == null) {
		Column(
			modifier = Modifier.fillMaxSize()
		) {
			Loader(false)
		}
	} else {
		task?.let {
			Column(
				modifier = Modifier.padding(16.dp).fillMaxSize()
			) {
				if (it.picture != null) {
					SubcomposeAsyncImage(
						model = it.picture,
						contentDescription = "Image",
						modifier = Modifier
							.fillMaxWidth()
							.clip(RoundedCornerShape(15.dp))
							.height(250.dp),
						contentScale = ContentScale.Crop,
						loading = { Loader(false) },
						error = { Text("Erreur de chargement", color = Color.Red) }
					)
					Spacer(modifier = Modifier.height(16.dp))
				}
				if (it.priority != null) {
					ButtonPriority(it.priority)
				}
				Spacer(modifier = Modifier.height(16.dp))
				Text(
					text = it.title.cap(),
					fontSize = 20.sp,
					fontFamily = Lato,
					fontWeight = FontWeight.SemiBold,
					color = colors.primaryText
				)
				Text(
					text = it.description.cap(),
					fontSize = 16.sp,
					fontFamily = Lato,
					fontWeight = FontWeight.Medium,
					color = colors.primaryText
				)
				Spacer(modifier = Modifier.height(16.dp))
				HorizontalDivider(
					color = Color.LightGray,
					thickness = 2.dp
				)
				Spacer(modifier = Modifier.height(10.dp))
				Row(
					modifier = Modifier.fillMaxWidth()
				) {
					TextButton(
						colors = ButtonDefaults.textButtonColors(
							containerColor = colors.error
						),
						enabled = !deleteLoading,
						elevation = ButtonDefaults.buttonElevation(3.dp),
						onClick = {
							deleteLoading = true
							coroutineScope.launch {
								val listUID = task?.listUID
								listUID?.let {
									task?.let { safeTask ->
										taskViewModel.deleteTask(safeTask, listUID) { success, message ->
											if (success && message != null) {
												navController.popBackStack()
											}
										}
									}
								}
							}
						}
					) {
						if (deleteLoading) {
							CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
						} else {
							Text(
								modifier = Modifier.padding(horizontal = 6.dp),
								textAlign = TextAlign.Center,
								text = "Supprimer",
								fontSize = 15.sp,
								fontFamily = Lato,
								fontWeight = FontWeight.SemiBold,
								color = Color.White
							)
						}
					}
					Spacer(modifier = Modifier.width(8.dp))
					Btn(
						modifier = Modifier.fillMaxWidth().weight(1f),
						text = "Modifier",
						colorLoading = Color.White,
						onClick = {
							isLoadingUpdateTask = true
							showUpdateTask = true
						},
						backgroundColor = colors.primary,
						textColor = Color.White,
						textSize = 15.sp,
					)
				}

				if (showUpdateTask) {
					ModalBottomSheet(
						modifier = Modifier.fillMaxSize(),
						sheetState = sheetState,
						containerColor = colors.background,
						scrimColor = Color.Black.copy(alpha = 0.5f),
						onDismissRequest = { showUpdateTask = false },
					) {
						val keyboardController = LocalSoftwareKeyboardController.current
						isLoadingUpdateTask = false

						task?.let { safeTask ->
							TaskForm(
								keyboardController, colors, safeTask, coroutineScope, sheetState,
								taskViewModel, snackbarHostState, user
							) {
								showUpdateTask = false
							}
						}
					}
				}
			}
		}
	}
}