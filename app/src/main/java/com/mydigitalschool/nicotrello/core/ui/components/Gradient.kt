package com.mydigitalschool.nicotrello.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.mydigitalschool.nicotrello.ui.theme.LocalColors

/**
 * Composant dégradé
 */

@Composable
fun Gradient(content: @Composable () -> Unit) {
	val colors = LocalColors.current

	val gradient = Brush.linearGradient(
		colors = listOf(colors.gradientTop, colors.gradientBottom)
	)

	Box(
		modifier = Modifier.fillMaxSize().background(gradient)
	) {
		content()
	}
}