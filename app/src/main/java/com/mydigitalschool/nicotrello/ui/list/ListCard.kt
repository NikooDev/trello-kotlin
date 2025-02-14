package com.mydigitalschool.nicotrello.ui.list

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.core.ui.components.Btn
import com.mydigitalschool.nicotrello.core.ui.components.Input
import com.mydigitalschool.nicotrello.core.ui.components.Loader
import com.mydigitalschool.nicotrello.core.utils.cap
import com.mydigitalschool.nicotrello.data.model.ListModel
import com.mydigitalschool.nicotrello.data.model.Priority
import com.mydigitalschool.nicotrello.data.model.TaskModel
import com.mydigitalschool.nicotrello.data.model.UserModel
import com.mydigitalschool.nicotrello.ui.task.TaskCard
import com.mydigitalschool.nicotrello.ui.task.TaskViewModel
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme
import com.mydigitalschool.nicotrello.ui.theme.Lato
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCard(
	navController: NavHostController,
	list: ListModel,
	user: UserModel?,
	projectUID: String,
	colors: ColorScheme,
	snackbarHostState: SnackbarHostState,
	taskViewModel: TaskViewModel,
	coroutineScope: CoroutineScope,
	showUpdateList: Boolean,
	tasks: List<TaskModel>,
	taskLoading: Boolean,
	onClose: () -> Unit,
	onClick: () -> Unit
) {
	var showCreateTask by remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState(
		skipPartiallyExpanded = true
	)
	var taskName by remember { mutableStateOf("") }
	var taskDescription by remember { mutableStateOf("") }
	var taskPicture by remember { mutableStateOf<Uri?>(null) }
	var taskPriority by remember { mutableStateOf<Priority?>(null) }

	var isLoading by remember { mutableStateOf(false) }
	var isLoadingAddTask by remember { mutableStateOf(false) }

	val context = LocalContext.current

	val imagePickerLauncher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.GetContent(),
		onResult = { uri ->
			uri?.let { taskPicture = it }
		}
	)

	fun handlePriority(priority: Priority) {
		taskPriority = if (taskPriority == priority) null else priority
	}

	key(list.uid) {
		Column(
			modifier = Modifier.fillMaxWidth()
		) {
			AnimatedVisibility(visible = !showUpdateList) {
				Row(
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
				) {
					Button(
						modifier = Modifier.weight(1f),
						colors = ButtonDefaults.buttonColors(
							containerColor = Color.Transparent,
							contentColor = Color.DarkGray
						),
						onClick = {
							onClick()
						}
					) {
						Text(
							text = list.title.uppercase(),
							fontSize = 18.sp,
							fontWeight = FontWeight.SemiBold,
							maxLines = 1,
							overflow = TextOverflow.Ellipsis,
							fontFamily = Lato
						)
					}
					IconButton(
						onClick = {
							onClick()
						}
					) {
						Image(
							painter = painterResource(R.drawable.edit),
							contentDescription = "Edit",
							modifier = Modifier.size(30.dp),
							colorFilter = ColorFilter.tint(Color.DarkGray)
						)
					}
				}
			}

			AnimatedVisibility(visible = showUpdateList) {
				ListForm(colors, true, projectUID, coroutineScope, snackbarHostState, list) {
					onClose()
				}
			}

			Spacer(modifier = Modifier.height(8.dp))

			AnimatedVisibility(
				visible = taskLoading,
				enter = fadeIn(),
				exit = fadeOut()
			) {
				Loader(false)
			}

			if (!taskLoading && tasks.isNotEmpty()) {
				Btn(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 8.dp),
					text = "Ajouter une tâche",
					loading = isLoadingAddTask,
					colorLoading = Color.White,
					onClick = {
						isLoadingAddTask = true
						showCreateTask = true
					},
					backgroundColor = colors.primary,
					textColor = Color.White,
					textSize = 15.sp,
				)
				LazyColumn(
					modifier = Modifier.fillMaxSize()
				) {
					itemsIndexed(tasks, key = { _, task -> task.uid!! }) { index, task ->
						TaskCard(navController, task, Modifier, tasks.lastIndex, index, colors)
					}

					item {
						Spacer(modifier = Modifier.height(16.dp))
					}
				}
			}

			if (tasks.isEmpty()) {
				Btn(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
					text = "Ajouter une tâche",
					onClick = {
						showCreateTask = true
					},
					backgroundColor = colors.primary,
					textColor = Color.White,
					textSize = 15.sp,
				)
				Spacer(modifier = Modifier.weight(1f))
				Text(
					modifier = Modifier.fillMaxWidth(),
					text = "Aucune tâche",
					fontSize = 25.sp,
					fontFamily = Lato,
					color = colors.secondaryText,
					textAlign = TextAlign.Center
				)
				Spacer(modifier = Modifier.weight(1f))
			}

			if (showCreateTask && list.uid != null) {
				ModalBottomSheet(
					modifier = Modifier.fillMaxSize(),
					sheetState = sheetState,
					containerColor = colors.background,
					scrimColor = Color.Black.copy(alpha = 0.5f),
					onDismissRequest = { showCreateTask = false },
				) {
					val keyboardController = LocalSoftwareKeyboardController.current
					isLoadingAddTask = false

					Box(modifier = Modifier.fillMaxSize()) {
						Column(
							modifier = Modifier
								.fillMaxWidth().fillMaxSize()
								.verticalScroll(rememberScrollState())
								.padding(horizontal = 16.dp).padding(bottom = 16.dp).pointerInput(Unit){
									detectTapGestures {
										keyboardController?.hide()
									}
								}
						) {
							Text(
								text = "Ajouter une tâche",
								fontSize = 20.sp,
								fontWeight = FontWeight.SemiBold,
								color = colors.primaryText,
								fontFamily = Lato
							)
							Spacer(modifier = Modifier.height(24.dp))
							Input(
								value = taskName,
								placeholder = "Titre de la tâche",
								modifier = Modifier,
								onValueChange = {
									taskName = it
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
							Input(
								value = taskDescription,
								placeholder = "Description",
								modifier = Modifier,
								onValueChange = {
									taskDescription = it
								},
								maxLines = 3,
								enabled = !isLoading,
								singleLine = false,
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
							Text(
								text = "Priorité",
								fontSize = 17.sp,
								fontWeight = FontWeight.SemiBold,
								color = colors.primaryText,
								fontFamily = Lato
							)
							Spacer(modifier = Modifier.height(8.dp))
							Row(
								modifier = Modifier.fillMaxWidth(),
								verticalAlignment = Alignment.CenterVertically
							) {
								Btn(
									modifier = Modifier.weight(1f),
									text = "Haute",
									enabled = !isLoading,
									onClick = {
										handlePriority(Priority.HIGH)
									},
									backgroundColor = if (taskPriority == Priority.HIGH) colors.priorityHigh else Color.Gray,
									textColor = Color.White,
									textSize = 15.sp,
								)
								Spacer(modifier = Modifier.width(8.dp))
								Btn(
									modifier = Modifier.weight(1f),
									text = "Moyenne",
									enabled = !isLoading,
									onClick = {
										handlePriority(Priority.MEDIUM)
									},
									backgroundColor = if (taskPriority == Priority.MEDIUM) colors.priorityMedium else Color.Gray,
									textColor = Color.White,
									textSize = 15.sp,
								)
								Spacer(modifier = Modifier.width(8.dp))
								Btn(
									modifier = Modifier.weight(1f),
									text = "Basse",
									enabled = !isLoading,
									onClick = {
										handlePriority(Priority.LOW)
									},
									backgroundColor = if (taskPriority == Priority.LOW) colors.priorityLow else Color.Gray,
									textColor = Color.White,
									textSize = 15.sp,
								)
							}
							Spacer(modifier = Modifier.height(16.dp))
							IconButton(
								modifier = Modifier.fillMaxWidth().shadow(8.dp, shape = CircleShape),
								enabled = !isLoading,
								onClick = {
									imagePickerLauncher.launch("image/*")
								},
								colors = IconButtonDefaults.iconButtonColors(
									containerColor = colors.primary,
									contentColor = Color.White,
									disabledContainerColor = colors.primary,
									disabledContentColor = Color.White
								)
							) {
								Row {
									Image(
										painter = painterResource(R.drawable.photo_landscape),
										contentDescription = "Picture",
										modifier = Modifier.size(24.dp),
										colorFilter = ColorFilter.tint(Color.White)
									)
									Spacer(modifier = Modifier.width(8.dp))
									Text(
										text = "Ajouter une photo",
										fontSize = 15.sp,
										fontWeight = FontWeight.SemiBold,
										color = Color.White,
										fontFamily = Lato
									)
								}
							}
							if (taskPicture != null) {
								Spacer(modifier = Modifier.height(16.dp))
								Box(
									modifier = Modifier.fillMaxWidth().height(250.dp)
								) {
									IconButton(
										onClick = {
											taskPicture = null
										},
										enabled = !isLoading,
										modifier = Modifier
											.zIndex(1f)
											.align(Alignment.TopEnd)
											.padding(8.dp),
										colors = IconButtonDefaults.iconButtonColors(
											containerColor = Color.White.copy(alpha = 0.6f),
											disabledContainerColor = Color.White.copy(alpha = 0.6f),
										)
									) {
										Image(
											painter = painterResource(R.drawable.delete),
											contentDescription = "Close",
											modifier = Modifier.size(24.dp),
											colorFilter = ColorFilter.tint(Color.Black.copy(alpha = 0.8f))
										)
									}

									SubcomposeAsyncImage(
										model = taskPicture,
										contentDescription = "Image",
										modifier = Modifier
											.fillMaxWidth()
											.clip(RoundedCornerShape(15.dp))
											.height(250.dp),
										contentScale = ContentScale.Crop,
										loading = { Loader(false) },
										error = { Text("Erreur de chargement", color = Color.Red) }
									)
								}
							}
							Spacer(modifier = Modifier.height(16.dp))
							Row {
								Btn(
									text = "Annuler",
									colorLoading = Color.White,
									paddingVertical = 5,
									backgroundColor = Color.Gray,
									textColor = Color.White,
									textSize = 15.sp,
									enabled = !isLoading,
									paddingHorizontal = 25,
									onClick = {
										coroutineScope.launch {
											sheetState.hide()
											delay(200)
											showCreateTask = false
										}
									}
								)
								Spacer(modifier = Modifier.width(8.dp))
								Btn(
									modifier = Modifier.weight(1f),
									text = "Ajouter la tâche",
									enabled = !isLoading,
									loading = isLoading,
									colorLoading = Color.White,
									onClick = {
										if (taskName.isNotEmpty() && taskDescription.isNotEmpty()) {
											user?.let {
												list.uid?.let {
													isLoading = true
													val newTask = TaskModel.create(
														userUID = list.userUID,
														projectUID = list.projectUID,
														listUID = list.uid as String,
														author = "${user.firstname.cap()} ${user.lastname.cap()}",
														title = taskName,
														description = taskDescription,
														priority = if (taskPriority != null) taskPriority else null,
														picture = if (taskPicture != null) taskPicture.toString() else null
													)

													coroutineScope.launch {
														taskViewModel.addTask(newTask, taskPicture, context) { success, message ->
															if (success && message != null) {
																keyboardController?.hide()
																taskName = ""
																taskDescription = ""
																taskPicture = null
																taskPriority = null
																coroutineScope.launch {
																	sheetState.hide()
																	showCreateTask = false
																	snackbarHostState.showSnackbar(
																		message = message,
																		duration = SnackbarDuration.Short
																	)
																	isLoading = false
																}
															}
														}
													}
												}
											}
										} else {
											coroutineScope.launch {
												snackbarHostState.showSnackbar("Veuillez remplir tous les champs")
											}
										}
									},
									backgroundColor = colors.primary,
									textColor = Color.White,
									textSize = 15.sp
								)
							}
						}

						SnackbarHost(modifier = Modifier.align(Alignment.BottomCenter), hostState = snackbarHostState) { data ->
							Snackbar(
								shape = RoundedCornerShape(15.dp),
								snackbarData = data,
								contentColor = Color.White
							)
						}
					}
				}
			}
		}
	}
}