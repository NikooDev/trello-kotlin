package com.mydigitalschool.nicotrello.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Avatar(
	name: String,
	size: Dp = 64.dp,
	backgroundColor: Color = MaterialTheme.colorScheme.onSecondaryContainer,
	textColor: Color = Color.White
) {
	val initials = name.split(" ")
		.filter { it.isNotEmpty() }
		.map { it.first().uppercaseChar() }
		.take(2)
		.joinToString("")

	Box(
		modifier = Modifier
			.size(size)
			.border(3.dp, Color.White, CircleShape)
			.shadow(elevation = 8.dp, shape = CircleShape)
			.clip(CircleShape)
			.background(backgroundColor),
		contentAlignment = Alignment.Center
	) {
		Text(
			text = initials,
			color = textColor,
			fontSize = (size.value / 3).sp,
			fontWeight = FontWeight.Bold,
		)
	}
}