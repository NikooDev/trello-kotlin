package com.mydigitalschool.nicotrello.ui.navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mydigitalschool.nicotrello.core.utils.enter
import com.mydigitalschool.nicotrello.core.utils.exit
import com.mydigitalschool.nicotrello.core.utils.popEnter
import com.mydigitalschool.nicotrello.core.utils.popExit
import com.mydigitalschool.nicotrello.data.model.ScreenUserModel
import com.mydigitalschool.nicotrello.ui.app.AppViewModel
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel
import com.mydigitalschool.nicotrello.ui.list.ListScreen
import com.mydigitalschool.nicotrello.ui.profile.ProfileScreen
import com.mydigitalschool.nicotrello.ui.project.AddProjectScreen
import com.mydigitalschool.nicotrello.ui.project.ProjectsScreen
import com.mydigitalschool.nicotrello.ui.task.TaskScreen
import com.mydigitalschool.nicotrello.ui.theme.LocalColors

/**
 * Navigation pour screens authentifiés
 */

@Composable
fun UserNavigation(appViewModel: AppViewModel, authViewModel: AuthViewModel) {
	val navController = rememberNavController()
	val backStackEntry by navController.currentBackStackEntryAsState()
	val snackbarHostState = remember { SnackbarHostState() }
	val colors = LocalColors.current
	val context = LocalContext.current

	/**
	 * Action sur le back swipe / button
	 *
	 * Si la stack est vide alors on met l'application en background
	 * Sinon on popBack
	 */
	BackHandler {
		if (navController.previousBackStackEntry == null) {
			(context as? Activity)?.moveTaskToBack(true)
		} else {
			navController.navigateUp()
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
		},
		topBar = {
			TopBar(navController, backStackEntry, appViewModel, authViewModel, colors)
		},
		bottomBar = {
			BottomBar(navController, colors, backStackEntry)
		}
	) { paddingValues ->
		Box(
			modifier = Modifier.fillMaxSize().padding(paddingValues).background(colors.background)
		) {
			NavHost(
				navController = navController,
				startDestination = ScreenUserModel.Projects.route
			) {
				composable(
					route = ScreenUserModel.Projects.route,
					enterTransition = { enter() },
					exitTransition = { exit() },
					popEnterTransition = { popEnter() },
					popExitTransition = { popExit() }
				) {
					ProjectsScreen(navController, snackbarHostState, authViewModel, colors) {
						appViewModel.setTitle("Mes projets")
					}
				}

				composable(
					route = "${ScreenUserModel.List.route}/{projectUID}",
					enterTransition = { enter() },
					exitTransition = { exit() },
					popEnterTransition = { popEnter() },
					popExitTransition = { popExit() }
				) { backStackEntry ->
					val projectUID = backStackEntry.arguments?.getString("projectUID") ?: return@composable

					ListScreen(navController, snackbarHostState, colors, projectUID) { title ->
						appViewModel.setTitle(title)
					}
				}

				composable(
					route = ScreenUserModel.AddProject.route,
					enterTransition = { enter() },
					exitTransition = { exit() },
					popEnterTransition = { popEnter() },
					popExitTransition = { popExit() }
				) {
					AddProjectScreen(navController, authViewModel, snackbarHostState, colors) {
						appViewModel.setTitle("Ajouter un projet")
					}
				}

				composable(
					route = "${ScreenUserModel.Task.route}/{taskUID}",
					enterTransition = { enter() },
					exitTransition = { exit() },
					popEnterTransition = { popEnter() },
					popExitTransition = { popExit() }
				) { backStackEntry ->
					val taskUID = backStackEntry.arguments?.getString("taskUID") ?: return@composable

					TaskScreen(navController, taskUID, snackbarHostState, colors) { title ->
						appViewModel.setTitle(title)
					}
				}

				composable(
					route = ScreenUserModel.Profile.route,
					enterTransition = { enter() },
					exitTransition = { exit() },
					popEnterTransition = { popEnter() },
					popExitTransition = { popExit() }
				) {
					ProfileScreen {
						appViewModel.setTitle("Mon profil")
					}
				}
			}
		}
	}
}