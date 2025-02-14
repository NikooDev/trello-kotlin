package com.mydigitalschool.nicotrello.data.model

import android.os.Bundle
import android.util.Log
import androidx.navigation.NavHostController
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.ui.app.AppViewModel
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel

/**
 * Action TopBar
 */
data class NavigationTopBarModel(
	val iconTopBar: Int = 0,
	val iconSize: Int = 35,
	val iconDescription: String = "",
	val route: String = "",
	val onClick: () -> Unit = {}
) {
	fun getTopBarRightActions(navController: NavHostController, arguments: Bundle?, appViewModel: AppViewModel, authViewModel: AuthViewModel): List<NavigationTopBarModel> {
		return listOf(
			NavigationTopBarModel(
				iconTopBar = R.drawable.turn_off,
				iconDescription = "Logout",
				route = ScreenUserModel.Profile.route,
				onClick = {
					appViewModel.setAppLoading(true)
					authViewModel.setAuthStatus(AuthModel.Unauthenticated)
					authViewModel.logout()
				}
			),
			/*NavigationTopBarModel(
				iconTopBar = R.drawable.edit,
				iconSize = 30,
				iconDescription = "Edit",
				route = "${ScreenUserModel.List.route}/{projectUID}",
				onClick = {
					val params = arguments?.getString("projectUID")


				}
			)*/
		)
	}
}

/**
 * Action Navigation BottomBar
 */
data class NavigationBottomBarModel(
	val iconBottomBar: Int = 0,
	val iconDescription: String = "",
	val route: String = ""
) {
	fun getBottomBarTabs(): List<NavigationBottomBarModel> {
		return listOf(
			NavigationBottomBarModel(
				iconBottomBar = R.drawable.layers,
				iconDescription = "Projects",
				route = ScreenUserModel.Projects.route,
			),
			NavigationBottomBarModel(
				iconBottomBar = R.drawable.add,
				iconDescription = "AddProject",
				route = ScreenUserModel.AddProject.route,
			),
			NavigationBottomBarModel(
				iconBottomBar = R.drawable.account_circle,
				iconDescription = "Profile",
				route = ScreenUserModel.Profile.route,
			)
		)
	}
}