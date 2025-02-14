package com.mydigitalschool.nicotrello.ui.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.mydigitalschool.nicotrello.core.ui.components.Gradient
import com.mydigitalschool.nicotrello.core.ui.components.Loader

/**
 * Screen de chargement
 */

@Composable
fun LoaderScreen(isLoading: Boolean) {
	AnimatedVisibility(
		visible = isLoading,
		enter = fadeIn(tween(500)),
		exit = fadeOut(tween(500))
	) {
		Gradient {
			Loader(true)
		}
	}
}