package com.mydigitalschool.nicotrello.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import com.mydigitalschool.nicotrello.data.model.NavigationBottomBarModel
import com.mydigitalschool.nicotrello.data.model.ScreenUserModel
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme

@Composable
fun BottomBar(navController: NavHostController, colors: ColorScheme, backStackEntry: NavBackStackEntry?) {
	val currentDestinationEntry by rememberUpdatedState(backStackEntry)
	val currentDestination = currentDestinationEntry?.destination
	val currentRoute = currentDestinationEntry?.destination?.route ?: ""

	val tabs = NavigationBottomBarModel().getBottomBarTabs()

	NavigationBar (
		containerColor = Color.White,
		modifier = Modifier.drawBehind {
			drawRect(
				brush = Brush.verticalGradient(
					colors = listOf(Color.Transparent, Color.Black),
					startY = -20f,
					endY = size.height
				),
				topLeft = Offset(0f, -20f)
			)
		}.clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp)),
	) {
		tabs.forEach { screen ->
			NavigationBarItem(
				modifier = Modifier.height(40.dp),
				colors = NavigationBarItemDefaults.colors(
					unselectedIconColor = Color.DarkGray,
					selectedIconColor = colors.primary,
					indicatorColor = colors.primary.copy(alpha = 0.2f)
				),
				icon = {
					Icon(
						painter = painterResource(id = screen.iconBottomBar),
						contentDescription = screen.iconDescription,
						modifier = Modifier.height(40.dp).size(if (screen.route == ScreenUserModel.AddProject.route) 44.dp else 30.dp)
					)
				},
				label = null,
				selected = currentDestination?.hierarchy?.any { it.route == screen.route } ?: false,
				onClick = {
					if (currentRoute != screen.route) {
						navController.navigate(screen.route) {
							popUpTo(screen.route) {
								inclusive = true
							}
							launchSingleTop = true
							restoreState = true
						}
					}
				}
			)
		}
	}
}