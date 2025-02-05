package com.mydigitalschool.nicotrello.ui.screens.guest

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavController
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.data.model.ScreenModel
import com.mydigitalschool.nicotrello.manager.AuthManager
import com.mydigitalschool.nicotrello.ui.theme.bodyFontFamily
import com.mydigitalschool.nicotrello.viewmodel.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavController, snackbarHostState: SnackbarHostState, coroutineScope: CoroutineScope) {
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }
	var isLoading by remember { mutableStateOf(false) }
	var errorMessage by remember { mutableStateOf<String?>(null) }
	val focusRequester = remember { FocusRequester() }
	val authManager = AuthManager()
	val authViewModel = AuthViewModel(authManager)

	LaunchedEffect(Unit) {
		focusRequester.requestFocus()
	}

	LaunchedEffect(errorMessage) {
		if (errorMessage != null) {
			coroutineScope.launch {
				snackbarHostState.showSnackbar(
					message = errorMessage!!,
					duration = SnackbarDuration.Short
				)
			}
		}
	}

	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(id = R.drawable.login),
			contentDescription = "Login",
			modifier = Modifier.fillMaxWidth().absoluteOffset(x = 0.dp, y = (-50).dp).zIndex(1f),
			contentScale = ContentScale.Fit
		)

		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically
		) {
			IconButton(
				onClick = {
					navController.popBackStack()
				}
			) {
				Image(
					painter = painterResource(R.drawable.back_ui),
					contentDescription = "Back",
					modifier = Modifier.size(35.dp).offset(y = 2.dp),
					colorFilter = ColorFilter.tint(Color.White)
				)
			}

			Spacer(modifier = Modifier.weight(1f))

			Text(
				text = "Connexion",
				style = MaterialTheme.typography.displaySmall,
				color = Color.White,
				modifier = Modifier.padding(horizontal = 16.dp)
			)

			Spacer(modifier = Modifier.weight(1f))
		}

		Spacer(modifier = Modifier.height(16.dp))

		Box(
			modifier = Modifier
				.fillMaxWidth().padding(top = 10.dp).height(65.dp)
				.background(Color(0xFF000000).copy(alpha = 0.3f), shape = RoundedCornerShape(15.dp))
				.border(
					1.dp,
					color = Color(0xFFFFFFFF).copy(alpha = 0.5f),
					shape = RoundedCornerShape(15.dp)
				).padding(horizontal = 5.dp)
		) {
			TextField(
				value = email,
				onValueChange = { email = it },
				label = { Text("Email", fontFamily = bodyFontFamily, fontSize = 16.sp) },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
				modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
					.focusRequester(focusRequester),
				colors = OutlinedTextFieldDefaults.colors(
					focusedTextColor = Color.White,
					unfocusedTextColor = Color.White,
					focusedLabelColor = Color.LightGray,
					unfocusedLabelColor = Color.LightGray,
					focusedBorderColor = Color.Transparent,
					unfocusedBorderColor = Color.Transparent
				),
				textStyle = TextStyle(
					fontSize = 17.sp,
					fontWeight = FontWeight.Medium,
					fontFamily = bodyFontFamily
				),
				shape = RoundedCornerShape(30.dp)
			)
		}

		Spacer(modifier = Modifier.height(8.dp))

		Box(
			modifier = Modifier
				.fillMaxWidth().padding(top = 10.dp).height(65.dp)
				.background(Color(0xFF000000).copy(alpha = 0.3f), shape = RoundedCornerShape(15.dp))
				.border(
					1.dp,
					color = Color(0xFFFFFFFF).copy(alpha = 0.5f),
					shape = RoundedCornerShape(15.dp)
				).padding(horizontal = 5.dp)
		) {
			TextField(
				value = password,
				onValueChange = { password = it },
				label = { Text("Mot de passe", fontFamily = bodyFontFamily, fontSize = 16.sp) },
				visualTransformation = PasswordVisualTransformation(),
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
				modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
				colors = OutlinedTextFieldDefaults.colors(
					focusedTextColor = Color.White,
					unfocusedTextColor = Color.White,
					focusedLabelColor = Color.LightGray,
					unfocusedLabelColor = Color.LightGray,
					focusedBorderColor = Color.Transparent,
					unfocusedBorderColor = Color.Transparent
				),
				textStyle = TextStyle(
					fontSize = 17.sp,
					fontWeight = FontWeight.Medium,
					fontFamily = bodyFontFamily
				),
				shape = RoundedCornerShape(30.dp)
			)
		}

		Spacer(modifier = Modifier.height(16.dp))

		Button(
			onClick = {
				isLoading = true
				errorMessage = null

				authViewModel.login(email, password) { success, error ->
					if (success) {
						isLoading = true
						Log.d("LoginSuccess", "Connecté")
					} else {
						isLoading = false
						errorMessage = error ?: "Échec de la connexion"
					}
				}
			},
			modifier = Modifier.fillMaxWidth().height(50.dp).border(
				1.dp,
				shape = RoundedCornerShape(15.dp),
				color = Color(0xFFFFFFFF).copy(alpha = 0.5f)
			),
			enabled = !isLoading && email.isNotBlank() && password.isNotBlank(),
			shape = RoundedCornerShape(15.dp),
			colors = ButtonColors(
				contentColor = Color.White,
				containerColor = MaterialTheme.colorScheme.onSecondaryContainer,
				disabledContentColor = Color.LightGray,
				disabledContainerColor = Color(0xFF000000).copy(alpha = 0.2f)
			)
		) {
			if (isLoading) {
				CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
			} else {
				Text(
					text = "SE CONNECTER",
					color = Color.White,
					fontSize = 18.sp,
					fontWeight = FontWeight.SemiBold
				)
			}
		}

		Spacer(modifier = Modifier.height(10.dp))

		TextButton(
			onClick = {
				navController.navigate(ScreenModel.Signup.route)
			}
		) {
			Text(
				"Pas encore de compte ? Inscrivez-vous",
				color = Color.White,
				fontSize = 17.sp,
				fontFamily = bodyFontFamily
			)
		}
	}
}