package com.mydigitalschool.nicotrello.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mydigitalschool.nicotrello.core.ui.components.Gradient
import com.mydigitalschool.nicotrello.core.utils.enter
import com.mydigitalschool.nicotrello.core.utils.exit
import com.mydigitalschool.nicotrello.core.utils.popEnter
import com.mydigitalschool.nicotrello.core.utils.popExit
import com.mydigitalschool.nicotrello.data.model.ScreenModel
import com.mydigitalschool.nicotrello.ui.app.AppViewModel
import com.mydigitalschool.nicotrello.ui.auth.LoginScreen
import com.mydigitalschool.nicotrello.ui.auth.SignupScreen
import com.mydigitalschool.nicotrello.ui.home.HomeScreen

/**
 * Navigation screens visiteurs
 */

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GuestNavigation(appViewModel: AppViewModel) {
	val navController = rememberNavController()
	val snackbarHostState = remember { SnackbarHostState() }

	val appLoading by appViewModel.appLoading.collectAsState()

	/**
	 * AppLoading permet d'afficher un loader sur les routes authentifiées.
	 * Je le désactive et je réinitialise les titres au premier tab.
	 */
	LaunchedEffect(appLoading) {
		if (appLoading) {
			appViewModel.setAppLoading(false)
			appViewModel.setTitle("Mes projets")
		}
	}

	Scaffold(
		snackbarHost = {
			SnackbarHost(hostState = snackbarHostState) { data ->
				Snackbar(
					shape = RoundedCornerShape(15.dp),
					snackbarData = data,
					contentColor = Color.White
				)
			}
		}
	) {
		Gradient {
			NavHost(
				navController = navController,
				startDestination = ScreenModel.Home.route
			) {
				composable(
					route = ScreenModel.Home.route,
					enterTransition = { enter() },
					exitTransition = { exit() },
					popEnterTransition = { popEnter() },
					popExitTransition = { popExit() }
				) {
					HomeScreen(navController)
				}

				composable(
					route = ScreenModel.Login.route,
					enterTransition = { enter() },
					exitTransition = { exit() },
					popEnterTransition = { popEnter() },
					popExitTransition = { popExit() }
				) {
					LoginScreen(navController, snackbarHostState)
				}

				composable(
					route = ScreenModel.Signup.route,
					enterTransition = { enter() },
					exitTransition = { exit() },
					popEnterTransition = { popEnter() },
					popExitTransition = { popExit() }
				) {
					SignupScreen(navController, snackbarHostState)
				}
			}
		}
	}
}