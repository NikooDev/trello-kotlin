package com.mydigitalschool.nicotrello.ui.navigation

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mydigitalschool.nicotrello.core.ui.components.Loader
import com.mydigitalschool.nicotrello.core.ui.components.StatusBar
import com.mydigitalschool.nicotrello.data.model.AuthModel
import com.mydigitalschool.nicotrello.data.model.Guest
import com.mydigitalschool.nicotrello.data.model.Start
import com.mydigitalschool.nicotrello.data.model.User
import com.mydigitalschool.nicotrello.ui.app.AppViewModel
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel
import com.mydigitalschool.nicotrello.ui.app.LoaderScreen
import kotlinx.coroutines.delay

/**
 * Navigation
 * Par défault, l'écran de chargement s'affiche
 * Selon l'état de l'authentification, on navigue vers l'écran correspondant
 * Configuration de la StatusBar
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
	val navController = rememberNavController()
	val authViewModel: AuthViewModel = viewModel()
	val appViewModel: AppViewModel = viewModel()

	val authStatus by authViewModel.authStatus.collectAsState()
	val isLoading by authViewModel.isLoading.collectAsState()
	val appLoading by appViewModel.appLoading.collectAsState()

	/**
	 * Navigation selon l'état d'authentification
	 * Ajustement des délais en fonction de l'affichage du LoaderScreen
	 */
	LaunchedEffect(authStatus) {
		delay(1500)

		when (authStatus) {
			/**
			 * Screens non authentifiés ou
			 * Si l'utilisateur a perdu son accès authentifié
			 */
			is AuthModel.Unauthenticated -> {
				navController.navigate(Guest) {
					popUpTo(Start) {
						inclusive = true
					}
				}
			}

			/**
			 * Screens authentifiés
			 * popUpTo permet de supprimer le navigateur visiteur
			 */
			is AuthModel.Authenticated -> {
				navController.navigate(User) {
					popUpTo(Guest) {
						inclusive = true
					}
				}
			}

			/**
			 * Si l'authentification a échoué ou si l'utilisateur perds son accès après une inactivité
			 * -> Déconnexion
			 */
			is AuthModel.AuthenticationFailed -> {
				authViewModel.logout()
			}
			else -> {}
		}

		delay(200)
		authViewModel.setIsLoading(false)
	}

	StatusBar(isLoading, authStatus)

	Column(modifier = Modifier.fillMaxSize()) {
		AnimatedVisibility(
			visible = appLoading
		) {
			BasicAlertDialog(onDismissRequest = {}) {
				Loader(true)
			}
		}

		NavHost(navController = navController, startDestination = Start) {
			/**
			 * Screen de chargement
			 */
			composable(Start) {
				LoaderScreen(isLoading)
			}

			/**
			 * Screens non authentifiés
			 */
			composable(Guest) {
				GuestNavigation(appViewModel)
			}

			/**
			 * Screens authentifiés
			 */
			composable(User) {
				UserNavigation(appViewModel, authViewModel)
			}
		}
	}
}