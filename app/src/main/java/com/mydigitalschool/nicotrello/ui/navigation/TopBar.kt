package com.mydigitalschool.nicotrello.ui.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.core.utils.cap
import com.mydigitalschool.nicotrello.core.utils.shouldShowBackButton
import com.mydigitalschool.nicotrello.data.model.NavigationTopBarModel
import com.mydigitalschool.nicotrello.ui.app.AppViewModel
import com.mydigitalschool.nicotrello.ui.auth.AuthViewModel
import com.mydigitalschool.nicotrello.ui.theme.ColorScheme
import com.mydigitalschool.nicotrello.ui.theme.Paytone
import kotlin.math.roundToInt

/**
 * Composant TopBar + actions
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, backStackEntry: NavBackStackEntry?, appViewModel: AppViewModel, authViewModel: AuthViewModel, colors: ColorScheme) {
	val currentRoute = backStackEntry?.destination?.route ?: ""
	val arguments = backStackEntry?.arguments
	val configuration = LocalConfiguration.current

	val title by appViewModel.title.collectAsState()

	val showBackButton = currentRoute.shouldShowBackButton()

	val rightActions = NavigationTopBarModel().getTopBarRightActions(navController, arguments, appViewModel, authViewModel)

	TopAppBar(
		modifier = Modifier.clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)),
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = colors.primary,
			titleContentColor = Color.White,
			scrolledContainerColor = Color.Transparent,
			navigationIconContentColor = Color.White,
			actionIconContentColor = Color.White
		),
		title = {
			Row (
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth().layout { measurable, constraints ->
					val paddingCompensation = 16.dp.toPx().roundToInt()
					val adjustedConstraints = constraints.copy(
						maxWidth = constraints.maxWidth + paddingCompensation
					)
					val placeable = measurable.measure(adjustedConstraints)
					layout(placeable.width, placeable.height) {
						placeable.place(-paddingCompensation / 2, 0)
					}
				}
			) {
				AnimatedVisibility(
					visible = showBackButton
				) {
					IconButton(
						modifier = Modifier.requiredWidth(40.dp).padding(top = 3.dp),
						onClick = {
							navController.popBackStack()
						}
					) {
						Image(
							painter = painterResource(R.drawable.back_ui),
							contentDescription = "Back",
							modifier = Modifier.size(30.dp),
							colorFilter = ColorFilter.tint(Color.White)
						)
					}
				}
				AnimatedContent(
					targetState = title,
					transitionSpec = {
						fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
					},
					contentKey = {
						title
					},
					label = ""
				) { targetState ->
					Text(
						modifier = Modifier.width((configuration.screenWidthDp - 100).dp).padding(start = 10.dp),
						maxLines = 1,
						overflow = TextOverflow.Ellipsis,
						text = targetState.cap(),
						style = TextStyle(fontFamily = Paytone, fontSize = 20.sp),
					)
				}
			}
		},
		actions = {
			Row(
				modifier = Modifier.requiredWidth(45.dp).offset(y = 2.dp),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.End
			) {
				rightActions.forEach { action ->
					key(action.route) {
						AnimatedVisibility(
							visible = currentRoute == action.route,
							enter = fadeIn(animationSpec = tween(500)),
							exit = fadeOut(animationSpec = tween(500))
						) {
							IconButton(
								modifier = Modifier.padding(end = 5.dp),
								onClick = action.onClick
							) {
								Icon(
									painterResource(id = action.iconTopBar),
									modifier = Modifier.size(action.iconSize.dp),
									contentDescription = action.iconDescription
								)
							}
						}
					}
				}
			}
		}
	)
}