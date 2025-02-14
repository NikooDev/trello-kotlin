package com.mydigitalschool.nicotrello.core.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Composant Card
 */

@Composable
fun Item(modifier: Modifier, onClick: () -> Unit, content: @Composable () -> Unit) {
	ElevatedCard(
		onClick = onClick,
		modifier = modifier,
		shape = RoundedCornerShape(15.dp),
		elevation = CardDefaults.cardElevation(
			defaultElevation = 8.dp
		),
		colors = CardDefaults.cardColors(
			containerColor = Color.White,
			contentColor = Color.DarkGray,
			disabledContentColor = Color.Unspecified,
			disabledContainerColor = Color.Unspecified
		)
	) {
		content()
	}
}