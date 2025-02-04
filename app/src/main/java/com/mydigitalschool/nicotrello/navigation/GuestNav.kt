package com.mydigitalschool.nicotrello.navigation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mydigitalschool.nicotrello.data.model.ScreenModel
import com.mydigitalschool.nicotrello.ui.screens.guest.*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavGuest(navController: NavHostController, startDestination: String) {
	val context = LocalContext.current
	val gradient = Brush.linearGradient(
		colors = listOf(Color(0xFFAA17CB), Color(0xFF1D5CBE))
	)

	val snackbarHostState = remember { SnackbarHostState() }
	val coroutineScope = rememberCoroutineScope()

	SideEffect {
		val window = (context as Activity).window
		val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
		windowInsetsController.isAppearanceLightStatusBars = false
	}

	Scaffold (
		snackbarHost = {
			Box(
				modifier = Modifier.padding(horizontal = 20.dp),
				content = {
					SnackbarHost(
						hostState = snackbarHostState,
						modifier = Modifier.systemBarsPadding(),
						snackbar = { data ->
							Snackbar (
								content = {
									Text(
										modifier = Modifier.padding(start = 5.dp),
										text = data.visuals.message,
										color = Color.White,
										fontSize = 16.sp
									)
								},
								actionOnNewLine = true,
								shape = RoundedCornerShape(30.dp),
								containerColor = Color(0xFF000000).copy(0.3f),
								contentColor = Color.White
							)
						}
					)
				}
			)
		},
		content = {
			Box (
				modifier = Modifier.fillMaxSize().background(gradient)
			) {
				NavHost (navController = navController, startDestination = startDestination) {
					composable(
						route = ScreenModel.Home.route,
						enterTransition = {
							return@composable fadeIn(tween(1000))
						},
						popEnterTransition = {
							return@composable slideIntoContainer(
								AnimatedContentTransitionScope.SlideDirection.End, tween(700)
							)
						},
						exitTransition = {
							return@composable slideOutOfContainer(
								AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
							)
						}
					) {
						HomeScreen(navController)
					}

					composable(
						route = ScreenModel.Login.route,
						enterTransition = {
							return@composable slideIntoContainer(
								AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
							)
						},
						popEnterTransition = {
							return@composable slideIntoContainer(
								AnimatedContentTransitionScope.SlideDirection.End, tween(700)
							)
						},
						popExitTransition = {
							return@composable slideOutOfContainer(
								AnimatedContentTransitionScope.SlideDirection.End, tween(700)
							)
						},
						exitTransition = {
							return@composable slideOutOfContainer(
								AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
							)
						}
					) {
						LoginScreen(navController, snackbarHostState, coroutineScope)
					}

					composable(
						route = ScreenModel.Signup.route,
						enterTransition = {
							return@composable slideIntoContainer(
								AnimatedContentTransitionScope.SlideDirection.Start, tween(700)
							)
						},
						popExitTransition = {
							return@composable slideOutOfContainer(
								AnimatedContentTransitionScope.SlideDirection.End, tween(700)
							)
						}
					) {
						SignupScreen(navController)
					}
				}
			}
		}
	)
}