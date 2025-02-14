package com.mydigitalschool.nicotrello.core.utils

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally

const val speed = 500

fun enter(): EnterTransition {
	return slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(durationMillis = speed, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(durationMillis = speed))
}

fun exit(): ExitTransition {
	return slideOutHorizontally(targetOffsetX = { -it }, animationSpec = tween(durationMillis = speed, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(durationMillis = speed))
}

fun popEnter(): EnterTransition {
	return slideInHorizontally(initialOffsetX = { -it }, animationSpec = tween(durationMillis = speed, easing = FastOutSlowInEasing)) + fadeIn(animationSpec = tween(durationMillis = speed))
}

fun popExit(): ExitTransition {
	return slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(durationMillis = speed, easing = FastOutSlowInEasing)) + fadeOut(animationSpec = tween(durationMillis = speed))
}