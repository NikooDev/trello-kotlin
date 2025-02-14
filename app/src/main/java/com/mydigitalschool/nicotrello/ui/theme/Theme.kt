package com.mydigitalschool.nicotrello.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember

val ColorThemeScheme = ColorScheme(
	background = Background,
	primary = Primary,
	secondary = Secondary,
	gradientTop = GradientTop,
	gradientBottom = GradientBottom,
	primaryText = PrimaryText,
	secondaryText = SecondaryText,
	tertiaryText = TertiaryText,
	error = Error,
	priorityLow = PriorityLow,
	priorityMedium = PriorityMedium,
	priorityHigh = PriorityHigh
)

val LocalColors = compositionLocalOf { ColorThemeScheme }

@Composable
fun NicotrelloTheme(content: @Composable () -> Unit) {
	val colors = remember { ColorThemeScheme }

	CompositionLocalProvider (LocalColors provides colors) {
		MaterialTheme(
			typography = Typography,
			content = content
		)
	}
}