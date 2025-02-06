package com.mydigitalschool.nicotrello.navigation

import android.app.Activity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mydigitalschool.nicotrello.data.model.ScreenUserModel
import com.mydigitalschool.nicotrello.ui.screens.user.*
import com.mydigitalschool.nicotrello.ui.screens.user.project.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mydigitalschool.nicotrello.ui.theme.displayFontFamily
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.manager.AuthManager
import com.mydigitalschool.nicotrello.viewmodel.AuthViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mydigitalschool.nicotrello.viewmodel.TitleViewModel

/**
 * Séparer la TopBar du NavHost
 * Séparer la Navigation Bottom Tab du NavHost
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavUser(navController: NavHostController, startDestination: String) {
	val context = LocalContext.current
	val speedTransition = 500
	val authManager = AuthManager()
	val authViewModel = AuthViewModel(authManager)

	val snackbarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()

	SideEffect {
		val window = (context as Activity).window
		val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
		windowInsetsController.isAppearanceLightStatusBars = true
	}

	val items = listOf(
		ScreenUserModel.Projects,
		ScreenUserModel.AddProject,
		ScreenUserModel.Profile
	)

	val backStackEntry by navController.currentBackStackEntryAsState()
	val currentDestination = backStackEntry?.destination
	val currentRoute = backStackEntry?.destination?.route ?: ""

	val titleViewModel: TitleViewModel = viewModel()
	val title by titleViewModel.title.collectAsState()

	Scaffold (
		snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
		topBar = {
			Surface(
				modifier = Modifier.shadow(
					elevation = 8.dp,
					shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
				)
			) {
				TopAppBar(
					modifier = Modifier.clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
					colors = TopAppBarColors(
						containerColor = MaterialTheme.colorScheme.onSecondary,
						titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
						scrolledContainerColor = Color.Transparent,
						navigationIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
						actionIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
					),
					title = {
						Row (
							verticalAlignment = Alignment.CenterVertically,
							modifier = Modifier.fillMaxWidth()
						) {
							AnimatedVisibility(
								visible = currentRoute.startsWith("project/")
							) {
								IconButton(
									modifier = Modifier.padding(top = 3.dp),
									onClick = {
										navController.navigate(ScreenUserModel.Projects.route) {
											popUpTo(ScreenUserModel.Projects.route) { inclusive = true }
										}
									}
								) {
									Image(
										painter = painterResource(R.drawable.back_ui),
										contentDescription = "Back",
										modifier = Modifier.size(30.dp),
										colorFilter = ColorFilter.tint(Color.DarkGray)
									)
								}
							}
							AnimatedContent(
								targetState = title,
								transitionSpec = {
									fadeIn(animationSpec = tween(700)) togetherWith fadeOut(animationSpec = tween(700))
								},
								label = ""
							) { targetState ->
								Text(
									targetState,
									modifier = Modifier.padding(start = if (currentRoute.startsWith("project/")) 5.dp else 0.dp), // Padding conditionnel
									style = TextStyle(fontFamily = displayFontFamily, fontSize = 28.sp)
								)
							}
						}
					},
					actions = {
						AnimatedVisibility (
							visible = currentRoute == "Profile",
							enter = fadeIn(animationSpec = tween(700)),
							exit = fadeOut(animationSpec = tween(700))
						) {
							when (currentRoute) {
								"Profile" -> {
									IconButton(
										modifier = Modifier.padding(bottom = 10.dp, end = 5.dp),
										onClick = {
											authViewModel.logout()
										}
									) {
										Icon(
											painterResource(id = R.drawable.turn_off),
											modifier = Modifier.size(35.dp),
											contentDescription = "Déconnexion"
										)
									}
								}
							}
						}
					}
				)
			}
		},
		bottomBar = {
			NavigationBar (
				containerColor = Color.White,
				modifier = Modifier.drawBehind {
					drawRect(
						brush = Brush.verticalGradient(
							colors = listOf(Color.Transparent, Color.Black),
							startY = -30f,
							endY = size.height
						),
						topLeft = Offset(0f, -30f)
					)
				}.clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
			) {
				items.forEach { screen ->
					NavigationBarItem(
						modifier = Modifier.padding(top = 10.dp),
						icon = {
							Icon(
								painter = painterResource(id = screen.icon),
								contentDescription = screen.label,
								modifier = Modifier.height(55.dp).size(if (screen.route == ScreenUserModel.AddProject.route) 44.dp else 34.dp)
							)
						},
						label = null,
						selected = currentDestination?.hierarchy?.any {
							it.route == screen.route
						} ?: false,
						onClick = {
							navController.navigate(screen.route) {
								popUpTo(ScreenUserModel.Projects.route) {
									inclusive = true
								}
								launchSingleTop = false
								restoreState = true
							}
						}
					)
				}
			}
		}
	) { paddingValues ->
		Box(
			modifier = Modifier.fillMaxSize()
				.background(color = MaterialTheme.colorScheme.surfaceDim)
				.padding(paddingValues)
		) {
			NavHost(navController = navController, startDestination = startDestination) {
				composable(
					route = ScreenUserModel.Projects.route,
					enterTransition = {
						return@composable slideIntoContainer(
							AnimatedContentTransitionScope.SlideDirection.End, tween(speedTransition)
						)
					},
					popEnterTransition = {
						return@composable slideIntoContainer(
							AnimatedContentTransitionScope.SlideDirection.End, tween(speedTransition)
						)
					},
					exitTransition = {
						return@composable slideOutOfContainer(
							AnimatedContentTransitionScope.SlideDirection.Start, tween(speedTransition)
						)
					}
				) {
					ProjectsScreen(navController) {
						titleViewModel.setTitle("Projets")
					}
				}

				composable(
					route = ScreenUserModel.AddProject.route,
					enterTransition = {
						return@composable slideIntoContainer(
							AnimatedContentTransitionScope.SlideDirection.Start, tween(speedTransition)
						)
					},
					popEnterTransition = {
						return@composable slideIntoContainer(
							AnimatedContentTransitionScope.SlideDirection.Start, tween(speedTransition)
						)
					},
					exitTransition = {
						return@composable slideOutOfContainer(
							AnimatedContentTransitionScope.SlideDirection.End, tween(speedTransition)
						)
					}
				) {
					AddProjectScreen(navController, titleViewModel, snackbarHostState, coroutineScope)
				}

				composable(
					route = "${ScreenUserModel.Project.route}/{projectUID}",
					enterTransition = {
						return@composable slideIntoContainer(
							AnimatedContentTransitionScope.SlideDirection.Start, tween(speedTransition)
						)
					},
					popEnterTransition = {
						return@composable slideIntoContainer(
							AnimatedContentTransitionScope.SlideDirection.Start, tween(speedTransition)
						)
					},
					exitTransition = {
						return@composable slideOutOfContainer(
							AnimatedContentTransitionScope.SlideDirection.End, tween(speedTransition)
						)
					}
				) { backStackEntry ->
					val projectUID = backStackEntry.arguments?.getString("projectUID") ?: return@composable
					ProjectScreen(navController, projectUID) { projectTitle ->
						titleViewModel.setTitle(projectTitle)
					}
				}

				composable(
					route = ScreenUserModel.Profile.route,
					enterTransition = {
						return@composable slideIntoContainer(
							AnimatedContentTransitionScope.SlideDirection.Start, tween(speedTransition)
						)
					},
					popEnterTransition = {
						return@composable slideIntoContainer(
							AnimatedContentTransitionScope.SlideDirection.Start, tween(speedTransition)
						)
					},
					exitTransition = {
						return@composable slideOutOfContainer(
							AnimatedContentTransitionScope.SlideDirection.End, tween(speedTransition)
						)
					}
				) {
					ProfileScreen(navController, titleViewModel)
				}
			}
		}
	}
}