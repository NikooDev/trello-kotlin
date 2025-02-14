package com.mydigitalschool.nicotrello.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mydigitalschool.nicotrello.ui.theme.Lato

/**
 * Composant input
 */

@Composable
fun Input(
	value: String,
	placeholder: String = "",
	onValueChange: (String) -> Unit,
	leadingIcon: ImageVector? = null,
	modifier: Modifier = Modifier,
	focusedTextColor: Color = Color.White,
	unfocusedTextColor: Color = Color.White,
	focusedLabelColor: Color = Color.LightGray,
	unfocusedLabelColor: Color = Color.LightGray,
	focusedBorderColor: Color = Color.Transparent,
	unfocusedBorderColor: Color = Color.Transparent,
	focusContainerColor: Color = Color.Black.copy(alpha = 0.5f),
	unfocusedContainerColor: Color = Color.Black.copy(alpha = 0.5f),
	cursorColor: Color = Color.DarkGray,
	elevation: Int = 8,
	shape: Int = 15,
	maxLines: Int = 1,
	enabled: Boolean = true,
	singleLine: Boolean = true,
	visualTransformation: VisualTransformation = VisualTransformation.None,
	keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
	keyboardActions: KeyboardActions = KeyboardActions.Default
) {
	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		leadingIcon = leadingIcon?.let {
			{ Icon(imageVector = it, contentDescription = null, tint = Color.Gray) }
		},
		placeholder = {
			Text(
				text = placeholder,
				fontSize = 17.sp,
				fontFamily = Lato,
				fontWeight = FontWeight.Thin
			)
		},
		enabled = enabled,
		keyboardOptions = keyboardOptions,
		keyboardActions = keyboardActions,
		visualTransformation = visualTransformation,
		colors = OutlinedTextFieldDefaults.colors(
			cursorColor = cursorColor,
			focusedContainerColor = focusContainerColor,
			unfocusedContainerColor = unfocusedContainerColor,
			focusedTextColor = focusedTextColor,
			unfocusedTextColor = unfocusedTextColor,
			focusedPlaceholderColor = focusedLabelColor,
			unfocusedPlaceholderColor = unfocusedLabelColor,
			focusedBorderColor = focusedBorderColor,
			unfocusedBorderColor = unfocusedBorderColor,
			disabledContainerColor = focusContainerColor,
			disabledBorderColor = unfocusedBorderColor,
			disabledTextColor = focusedTextColor.copy(alpha = 0.5f)
		),
		maxLines = maxLines,
		singleLine = singleLine,
		textStyle = TextStyle(
			fontSize = 17.sp,
			fontWeight = FontWeight.Medium,
			fontFamily = Lato
		),
		modifier = modifier.fillMaxWidth().shadow(elevation = elevation.dp, shape = RoundedCornerShape(shape.dp)),
		shape = RoundedCornerShape(shape.dp)
	)
}