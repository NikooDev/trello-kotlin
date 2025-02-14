package com.mydigitalschool.nicotrello.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mydigitalschool.nicotrello.ui.theme.Lato
import com.mydigitalschool.nicotrello.ui.theme.LocalColors

/**
 * Composant bouton
 */

@Composable
fun Btn(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	text: String,
	enabled: Boolean = true,
	textWeight: FontWeight = FontWeight.SemiBold,
	textSize: TextUnit = 16.sp,
	loading: Boolean = false,
	colorLoading: Color = LocalColors.current.primary,
	icon: ImageVector? = null,
	backgroundColor: Color = LocalColors.current.primary,
	textColor: Color = Color.White,
	shape: Shape = RoundedCornerShape(30.dp),
	paddingVertical: Int = 10,
	paddingHorizontal: Int = 15,
	elevation: Int = 8
) {
	val isTransparent = backgroundColor == Color.Transparent

	Button(
		onClick = onClick,
		modifier = modifier,
		elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = if (isTransparent) 0.dp else elevation.dp),
		colors = ButtonDefaults.buttonColors(
			containerColor = backgroundColor,
			contentColor = textColor,
			disabledContainerColor = backgroundColor,
			disabledContentColor = textColor
		),
		enabled = enabled,
		shape = shape,
		contentPadding = PaddingValues(vertical = paddingVertical.dp, horizontal = paddingHorizontal.dp)
	) {
		Row(verticalAlignment = Alignment.CenterVertically) {
			if (icon != null) {
				Icon(
					imageVector = icon,
					contentDescription = null,
					modifier = Modifier.size(18.dp)
				)
				Spacer(modifier = Modifier.size(8.dp))
			}
			if (loading) {
				CircularProgressIndicator(modifier = Modifier.size(20.dp), color = colorLoading, strokeWidth = 2.dp)
			} else {
				Text(text = text, fontWeight = textWeight, fontSize = textSize, fontFamily = Lato)
			}
		}
	}
}