package com.mydigitalschool.nicotrello.ui.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mydigitalschool.nicotrello.core.ui.components.Btn
import com.mydigitalschool.nicotrello.core.ui.components.Loader
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel
import com.mydigitalschool.nicotrello.ui.project.ProjectViewModel
import com.mydigitalschool.nicotrello.ui.task.TaskViewModel
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme
import com.mydigitalschool.nicotrello.ui.theme.Lato
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
	navController: NavHostController,
	snackbarHostState: SnackbarHostState,
	colors: ColorScheme,
	projectUID: String,
	onSetTitle: (title: String) -> Unit
) {
	val projectViewModel: ProjectViewModel = viewModel()
	val listViewModel: ListViewModel = viewModel()
	val taskViewModel: TaskViewModel = viewModel()
	val authViewModel: AuthViewModel = viewModel()

	var fetchJob: Job? = null
	val coroutineScope = rememberCoroutineScope()

	val project by projectViewModel.project.collectAsState()
	val lists by listViewModel.lists.collectAsState()
	val currentPage by listViewModel.currentPage.collectAsState()
	val isLoading by listViewModel.isLoading.collectAsState()
	val scrollTop by listViewModel.scrollTop.collectAsState()
	val error by listViewModel.error.collectAsState()
	val errorTask by taskViewModel.error.collectAsState()
	val user by authViewModel.user.collectAsState()

	var showCreateNewList by remember { mutableStateOf(false) }
	var showCreateList by remember { mutableStateOf(false) }
	var showUpdateList by remember { mutableStateOf(false) }
	val listsExists by remember { derivedStateOf { lists.isNotEmpty() } }

	val taskLoading by taskViewModel.isLoading.collectAsState()
	val tasks by taskViewModel.tasks.collectAsState()
	val userUID = authViewModel.getUserUID()

	val pagerState = rememberPagerState(
		initialPage = currentPage,
		pageCount = { lists.size }
	)

	LaunchedEffect(project) {
		project?.let { onSetTitle(it.title) }
	}

	LaunchedEffect(error, errorTask) {
		error?.let {
			coroutineScope.launch {
				snackbarHostState.showSnackbar(it)
			}
		}
		errorTask?.let {
			coroutineScope.launch {
				snackbarHostState.showSnackbar(it)
			}
		}
	}

	LaunchedEffect(Unit) {
		userUID?.let { authViewModel.getUserById(it) }
	}

	/**
	 * Récupère le projet et ses listes
	 */
	LaunchedEffect(projectUID) {
		if (projectViewModel.project.value == null) {
			fetchJob?.cancel()
			fetchJob = coroutineScope.launch {
				projectViewModel.getProjectById(projectUID)
				delay(500)
				listViewModel.getListsByProjet(projectUID)
			}
		}
	}

	LaunchedEffect(scrollTop) {
		if (scrollTop) {
			pagerState.scrollToPage(0)
			listViewModel.setScrollTop(false)
		}
	}

	Column (
		modifier = Modifier.fillMaxWidth().padding(top = 8.dp).navigationBarsPadding().pointerInput(Unit) {
			detectTapGestures {
				showCreateNewList = false
				showCreateList = false
				showUpdateList = false
			}
		},
		horizontalAlignment = Alignment.Start,
		verticalArrangement = Arrangement.Top
	) {
		AnimatedVisibility(
			visible = isLoading,
			enter = fadeIn(animationSpec = tween(300)),
			exit = fadeOut(animationSpec = tween(300))
		) {
			Loader(false)
		}

		if (!isLoading && listsExists) {
			Column(
				modifier = Modifier.fillMaxWidth().fillMaxHeight()
			) {
				HorizontalPager(
					state = pagerState,
					verticalAlignment = Alignment.Top,
					modifier = Modifier.fillMaxWidth().weight(1f),
					key = { index -> lists.getOrNull(index)?.uid ?: index.toString() }
				) { page ->
					val list = lists[page]
					val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
					val pagerLoading = pageOffset != 0f

					LaunchedEffect(page) {
						listViewModel.setCurrentPage(page)
						showCreateNewList = false
						showCreateList = false
						showUpdateList = false
					}

					/**
					 * Lorsque la page change -> récupération des tâches
					 */
					LaunchedEffect(pagerState, list.uid) {
						snapshotFlow { pagerState.currentPage }.collect { currentPage ->
							val listUID = lists.getOrNull(currentPage)?.uid

							if (listUID != null) {
								taskViewModel.getTasksByList(listUID)
							}
						}
					}

					/**
					 * Si la page n'est pas affichée entièrement -> loader
					 */
					if (pagerLoading) {
						Loader(false)
					} else {
						ListCard(navController, list, user, projectUID, colors, snackbarHostState, taskViewModel,
							coroutineScope, showUpdateList, tasks, taskLoading, onClose = { showUpdateList = false }
						) {
							showCreateNewList = false
							showUpdateList = true
						}
					}
				}
				Spacer(modifier = Modifier.height(16.dp))
				Row(
					modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
					horizontalArrangement = Arrangement.Center
				) {
					repeat(pagerState.pageCount) { index ->
						val isSelected = pagerState.currentPage == index
						Box(
							modifier = Modifier
								.size(10.dp)
								.clip(CircleShape)
								.shadow(elevation = 8.dp, shape = CircleShape)
								.background(if (isSelected) colors.primary else Color.LightGray)
								.clickable {
									coroutineScope.launch {
										pagerState.animateScrollToPage(index)
									}
								}
						)
						Spacer(modifier = Modifier.width(4.dp))
					}
				}
				Spacer(modifier = Modifier.height(16.dp))
				AnimatedVisibility(visible = showCreateNewList) {
					ListForm(colors, false, projectUID, coroutineScope, snackbarHostState, null) {
						showCreateNewList = false
					}
				}
				AnimatedVisibility(visible = !showCreateNewList) {
					Btn(
						modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
						text = "Créer une nouvelle liste",
						onClick = {
							showCreateNewList = true
							showUpdateList = false
						},
						backgroundColor = colors.primary,
						textColor = Color.White,
						textSize = 15.sp,
					)
				}
			}
		}

		if (!isLoading && !listsExists) {
			Column (
				modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
				horizontalAlignment = Alignment.CenterHorizontally,
				verticalArrangement = Arrangement.Center
			) {
				Spacer(modifier = Modifier.weight(1f))
				Text(
					text = "Aucune liste",
					fontSize = 25.sp,
					fontFamily = Lato,
					color = colors.secondaryText,
					textAlign = TextAlign.Center
				)
				Spacer(modifier = Modifier.weight(1f))
				AnimatedVisibility(visible = showCreateList) {
					ListForm(colors, false, projectUID, coroutineScope, snackbarHostState, null) {
						showCreateList = false
					}
				}
				AnimatedVisibility(visible = !showCreateList) {
					Btn(
						modifier = Modifier.fillMaxWidth(),
						text = "Créer une liste",
						onClick = {
							showCreateList = true
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