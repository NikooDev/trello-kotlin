package com.mydigitalschool.nicotrello.ui.project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.core.ui.components.Btn
import com.mydigitalschool.nicotrello.core.ui.components.Input
import com.mydigitalschool.nicotrello.core.ui.components.Item
import com.mydigitalschool.nicotrello.core.utils.cap
import com.mydigitalschool.nicotrello.core.utils.toFormattedString
import com.mydigitalschool.nicotrello.data.model.ProjectModel
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme
import com.mydigitalschool.nicotrello.ui.theme.Lato
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
	project: ProjectModel,
	colors: ColorScheme,
	projectViewModel: ProjectViewModel,
	snackbarHostState: SnackbarHostState,
	coroutineScope: CoroutineScope,
	modifier: Modifier = Modifier,
	onClick: () -> Unit
) {
	val plurial = if (project.nbTasks > 1) "s" else ""
	var showUpdate by remember { mutableStateOf(false) }
	var showDialog by remember { mutableStateOf(false) }
	var projectName by remember { mutableStateOf(project.title) }

	val focusProjectName = remember { FocusRequester() }
	val keyboardController = LocalSoftwareKeyboardController.current

	/**
	 * Récupère le nombre de tâches du projet
	 */
	LaunchedEffect(project.uid) {
		coroutineScope.launch {
			project.uid?.let {
				projectViewModel.getCountTasksByProjet(it)
			}
		}
	}

	key(project.uid) {
		Item(
			modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
			onClick = onClick
		) {
			Column(
				modifier = modifier.padding(bottom = 10.dp).fillMaxWidth()
			) {
				AnimatedVisibility(modifier = modifier, visible = !showUpdate) {
					Row(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.Start
					) {
						Text(
							modifier = modifier.weight(1f),
							text = project.title,
							overflow = TextOverflow.Ellipsis,
							fontWeight = FontWeight.Bold,
							maxLines = 1,
							fontSize = 18.sp,
							fontFamily = Lato,
							color = colors.primaryText
						)
						IconButton(
							modifier = Modifier.offset(x = 15.dp),
							onClick = {
								showUpdate = true
							}
						) {
							Image(
								painter = painterResource(R.drawable.edit),
								contentDescription = "Edit",
								modifier = Modifier.size(24.dp),
								colorFilter = ColorFilter.tint(Color.DarkGray)
							)
						}
					}
				}
				AnimatedVisibility(visible = showUpdate) {
					Column(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp)
							.padding(top = 16.dp, bottom = 16.dp)
					) {
						Input(
							value = projectName,
							placeholder = "Titre du projet",
							modifier = Modifier.wrapContentHeight().focusRequester(focusProjectName),
							onValueChange = {
								projectName = it.cap()
							},
							unfocusedContainerColor = Color.White,
							focusContainerColor = Color.White,
							unfocusedLabelColor = Color.Gray,
							focusedLabelColor = Color.DarkGray,
							unfocusedTextColor = Color.DarkGray,
							focusedTextColor = Color.DarkGray,
							unfocusedBorderColor = Color.Transparent,
							focusedBorderColor = Color.Transparent
						)
						Spacer(modifier = Modifier.height(10.dp))
						Row {
							Btn(
								modifier = Modifier.height(35.dp),
								text = "Annuler",
								colorLoading = Color.White,
								paddingVertical = 5,
								backgroundColor = Color.Gray,
								textColor = Color.White,
								textSize = 13.sp,
								onClick = {
									showUpdate = false
								}
							)
							Spacer(modifier = Modifier.width(8.dp))
							Btn(
								modifier = Modifier.weight(1f).height(35.dp),
								text = "Modifier le projet",
								colorLoading = Color.White,
								paddingVertical = 5,
								backgroundColor = colors.primary,
								textColor = Color.White,
								textSize = 13.sp,
								enabled = projectName.isNotEmpty() || projectName != project.title,
								onClick = {
									if (projectName.isNotEmpty()) {
										keyboardController?.hide()

										val trimmedProjectName = projectName.trim()

										val updateProject = ProjectModel(
											title = trimmedProjectName,
											author = project.author,
											userUID = project.userUID,
											nbTasks = project.nbTasks,
											created = project.created
										)

										coroutineScope.launch {
											project.uid?.let {
												projectViewModel.updateProject(updateProject, it) { success, message ->
													if (success && message != null) {
														coroutineScope.launch {
															delay(500)
															snackbarHostState.showSnackbar(
																message = message,
																duration = SnackbarDuration.Short
															)
														}
														showUpdate = false
													}
												}
											}
										}
									}
								}
							)
						}
					}
				}
				Row(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.End
				) {
					AnimatedVisibility(visible = showUpdate) {
						IconButton(
							modifier = modifier.shadow(elevation = 8.dp, shape = CircleShape).height(35.dp)
								.width(35.dp),
							colors = IconButtonDefaults.iconButtonColors(
								containerColor = Color.Red,
								contentColor = Color.White
							),
							shape = CircleShape,
							onClick = {
								showDialog = true
							}
						) {
							Image(
								painter = painterResource(R.drawable.delete),
								contentDescription = "Delete",
								modifier = Modifier.size(20.dp),
								colorFilter = ColorFilter.tint(Color.White)
							)
						}
					}
					Column(
						modifier = Modifier.weight(1f),
						horizontalAlignment = Alignment.End
					) {
						Text(
							text = project.author,
							fontSize = 15.sp,
							fontWeight = FontWeight.SemiBold,
							fontFamily = Lato,
							overflow = TextOverflow.Ellipsis,
							maxLines = 1,
							lineHeight = 10.sp,
							color = colors.secondaryText
						)
						Text(
							text = "${project.nbTasks} tâche${plurial} • Créé le ${project.created?.toFormattedString()}",
							color = colors.tertiaryText,
							fontSize = 13.sp
						)
					}
				}
				if (showDialog) {
					BasicAlertDialog(
						modifier = Modifier.fillMaxWidth(),
						onDismissRequest = { showDialog = false },
						properties = DialogProperties(dismissOnClickOutside = false)
					) {
						Column(
							modifier = Modifier.fillMaxWidth()
								.background(color = Color.White, shape = RoundedCornerShape(15.dp))
								.padding(16.dp)
						) {
							Text(
								modifier = Modifier.fillMaxWidth(),
								text = "Supprimer le projet ${project.title}",
								fontSize = 17.sp,
								maxLines = 2,
								overflow = TextOverflow.Ellipsis,
								fontWeight = FontWeight.SemiBold,
								fontFamily = Lato
							)
							Spacer(modifier = Modifier.height(8.dp))
							Text(
								modifier = Modifier.fillMaxWidth(),
								text = "Les listes et tâches associées seront également supprimées.",
								fontSize = 15.sp,
								maxLines = 2,
								fontWeight = FontWeight.Medium,
								fontFamily = Lato
							)
							Spacer(modifier = Modifier.height(16.dp))
							Row {
								Btn(
									modifier = Modifier.weight(1f).height(35.dp),
									text = "Annuler",
									colorLoading = Color.White,
									paddingVertical = 5,
									backgroundColor = Color.Gray,
									textColor = Color.White,
									textSize = 13.sp,
									onClick = {
										showDialog = false
									}
								)
								Spacer(modifier = Modifier.width(8.dp))
								Btn(
									modifier = Modifier.weight(1f).height(35.dp),
									text = "Supprimer",
									colorLoading = Color.White,
									paddingVertical = 5,
									paddingHorizontal = 20,
									backgroundColor = Color.Red,
									textColor = Color.White,
									textSize = 13.sp,
									onClick = {
										coroutineScope.launch {
											project.uid?.let {
												projectViewModel.deleteProject(it) { success, message ->
													if (success && message != null) {
														coroutineScope.launch {
															snackbarHostState.showSnackbar(
																message = message,
																duration = SnackbarDuration.Short
															)
														}

														showDialog = false
													}
												}
											}
										}
									}
								)
							}
						}
					}
				}
			}
		}
	}
}