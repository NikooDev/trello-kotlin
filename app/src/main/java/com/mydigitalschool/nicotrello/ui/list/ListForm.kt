package com.mydigitalschool.nicotrello.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.core.ui.components.Btn
import com.mydigitalschool.nicotrello.core.ui.components.Input
import com.mydigitalschool.nicotrello.data.model.ListModel
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme
import com.mydigitalschool.nicotrello.ui.theme.Lato
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListForm(
	colors: ColorScheme,
	update: Boolean,
	projectUID: String,
	coroutineScope: CoroutineScope,
	snackbarHostState: SnackbarHostState,
	list: ListModel? = null,
	onClose: () -> Unit
) {
	val authViewModel: AuthViewModel = viewModel()
	val listViewModel: ListViewModel = viewModel()
	val listExist = (list != null && update)
	var listName by remember { mutableStateOf(list?.title ?: "") }
	val textButton = if (listExist) "Modifier la liste" else "Ajouter la liste"
	var showDeleteConfirm by remember { mutableStateOf(false) }

	val userUID = authViewModel.getUserUID()

	val paddingTop = if (update) 16.dp else 8.dp

	Column(
		modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp).padding(top = paddingTop, bottom = 16.dp)
	) {
		Input(
			value = listName,
			placeholder = "Titre de la liste",
			modifier = Modifier,
			onValueChange = {
				listName = it
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
			if (update) {
				IconButton (
					modifier = Modifier.height(35.dp).width(35.dp),
					colors = IconButtonDefaults.iconButtonColors(
						containerColor = Color.Red,
						contentColor = Color.White
					),
					onClick = {
						showDeleteConfirm = true
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
			Spacer(modifier = Modifier.weight(1f))
			Btn(
				modifier = Modifier.height(35.dp),
				text = "Annuler",
				colorLoading = Color.White,
				paddingVertical = 5,
				backgroundColor = Color.Gray,
				textColor = Color.White,
				textSize = 13.sp,
				onClick = {
					onClose()
				}
			)
			Spacer(modifier = Modifier.width(8.dp))
			Btn(
				modifier = Modifier.height(35.dp),
				text = textButton,
				colorLoading = Color.White,
				paddingVertical = 5,
				backgroundColor = colors.primary,
				textColor = Color.White,
				textSize = 13.sp,
				onClick = {
					if (listName.isNotEmpty() && userUID != null) {
						if (!update) {
							val newList = ListModel.create(
								title = listName,
								userUID = userUID,
								projectUID = projectUID
							)

							coroutineScope.launch {
								listViewModel.addList(newList) { success, message ->
									if (success && message != null) {
										listName = ""

										coroutineScope.launch {
											snackbarHostState.showSnackbar(
												message = message,
												duration = SnackbarDuration.Short
											)
										}

										onClose()
										listViewModel.setScrollTop(true)
									}
								}
							}
						} else {
							if (list != null && listName != list.title) {
								val listUID = list.uid

								val updateList = ListModel(
									title = listName,
									userUID = userUID,
									projectUID = projectUID,
									created = list.created
								)

								coroutineScope.launch {
									listUID?.let {
										listViewModel.updateList(updateList, it) { success, message ->
											if (success && message != null) {
												listName = ""

												coroutineScope.launch {
													snackbarHostState.showSnackbar(
														message = message,
														duration = SnackbarDuration.Short
													)
												}

												onClose()
											}
										}
									}
								}
							}
						}
					}
				}
			)
		}

		if (showDeleteConfirm && update && list != null) {
			BasicAlertDialog(
				modifier = Modifier.fillMaxWidth(),
				onDismissRequest = { showDeleteConfirm = false },
				properties = DialogProperties(dismissOnClickOutside = false)
			) {
				Column(
					modifier = Modifier.fillMaxWidth()
						.background(color = Color.White, shape = RoundedCornerShape(15.dp))
						.padding(16.dp)
				) {
					Text(
						modifier = Modifier.fillMaxWidth(),
						text = "Supprimer la liste \"${list.title}\"",
						fontSize = 17.sp,
						maxLines = 2,
						overflow = TextOverflow.Ellipsis,
						fontWeight = FontWeight.SemiBold,
						fontFamily = Lato
					)
					Spacer(modifier = Modifier.height(8.dp))
					Text(
						modifier = Modifier.fillMaxWidth(),
						text = "Les tâches associées seront également supprimées.",
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
								showDeleteConfirm = false
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
									list.uid?.let {
										listViewModel.deleteList(it) { success, message ->
											if (success && message != null) {
												coroutineScope.launch {
													snackbarHostState.showSnackbar(
														message = message,
														duration = SnackbarDuration.Short
													)
												}

												listViewModel.getListsByProjet(projectUID)

												showDeleteConfirm = false
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