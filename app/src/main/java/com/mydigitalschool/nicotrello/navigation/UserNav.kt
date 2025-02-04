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
import androidx.navigation.NavGraph.Companion.findStartDestination
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

	val backStackEntry = navController.currentBackStackEntryAsState()
	val currentRoute = backStackEntry.value?.destination?.route ?: "Unknown"

	val currentLabel = when (currentRoute) {
		ScreenUserModel.Projects.route -> ScreenUserModel.Projects.label
		ScreenUserModel.AddProject.route -> ScreenUserModel.AddProject.label
		ScreenUserModel.Profile.route -> ScreenUserModel.Profile.label
		else -> ""
	}

	Scaffold (
		snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
		topBar = {
			Surface(
				modifier = Modifier.shadow(
					elevation = 8.dp,
					shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
				)
			) {
				AnimatedContent (
					targetState = currentLabel,
					transitionSpec = {
						fadeIn(animationSpec = tween(700)) togetherWith fadeOut(animationSpec = tween(700))
					},
					label = ""
				) { targetState ->
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
							Text(targetState, modifier = Modifier.padding(start = 5.dp, bottom = 15.dp), style = TextStyle(
								fontFamily = displayFontFamily,
								fontSize = 28.sp
							))
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
				val currentDestination = backStackEntry.value?.destination
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
								popUpTo(navController.graph.findStartDestination().id) {
									saveState = true
								}
								launchSingleTop = true
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
					ProjectsScreen(navController)
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
					AddProjectScreen(navController)
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
					ProfileScreen(navController)
				}
			}
		}
	}
}