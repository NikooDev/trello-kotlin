package com.mydigitalschool.nicotrello.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mydigitalschool.nicotrello.ui.theme.LocalColors

@Composable
fun Loader(darkmode: Boolean) {
	val colors = LocalColors.current
	val progressColor = if (darkmode) Color.White else colors.primary

	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center
	) {
		CircularProgressIndicator(
			modifier = Modifier.size(40.dp),
			color = progressColor
		)
	}
}