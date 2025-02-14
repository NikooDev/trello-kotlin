package com.mydigitalschool.nicotrello.core.ui.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.mydigitalschool.nicotrello.ui.theme.Paytone

@Composable
fun Title(text: String, fontSize: Int) {
	Text(
		text = text,
		fontFamily = Paytone,
		fontSize = fontSize.sp,
		color = Color.White,
		lineHeight = 30.sp,
		textAlign = TextAlign.Center,
		style = LocalTextStyle.current.copy(
			color = Color.Black,
			shadow = Shadow(
				color = Color.Black,
				offset = Offset(0f, 5f),
				blurRadius = 25f
			)
		)
	)
}