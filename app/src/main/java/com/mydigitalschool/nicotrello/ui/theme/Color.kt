package com.mydigitalschool.nicotrello.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class ColorScheme(
	val background: Color,
	val primary: Color,
	val secondary: Color,
	val gradientTop: Color,
	val gradientBottom: Color,
	val primaryText: Color,
	val secondaryText: Color,
	val tertiaryText: Color,
	val error: Color,
	val priorityLow: Color,
	val priorityMedium: Color,
	val priorityHigh: Color
)

val Background = Color(0xFFF2F4F7)
val Primary = Color(0xFF1D5CBE)
val Secondary = Color(0xFF1D5CBE)
val GradientTop = Color(0xFF721EE5)
val GradientBottom = Color(0xFFD110FF)
val PrimaryText = Color(0xFF080809)
val SecondaryText = Color(0xFF3A3A3D)
val TertiaryText = Color(0xFF727277)
val Error = Color(0xFFD53030)
val PriorityLow = Color(0xFF21CC38)
val PriorityMedium = Color(0xFFFCA022)
val PriorityHigh = Color(0xFFFF4747)