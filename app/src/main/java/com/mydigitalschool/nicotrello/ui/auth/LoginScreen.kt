package com.mydigitalschool.nicotrello.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.mydigitalschool.nicotrello.R
import com.mydigitalschool.nicotrello.core.ui.components.Btn
import com.mydigitalschool.nicotrello.core.ui.components.Input
import com.mydigitalschool.nicotrello.core.ui.components.Title
import com.mydigitalschool.nicotrello.data.model.AuthModel
import com.mydigitalschool.nicotrello.data.model.ScreenModel
import com.mydigitalschool.nicotrello.ui.theme.LocalColors
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, snackbarHostState: SnackbarHostState) {
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	val focusEmail = remember { FocusRequester() }
	val focusPassword = remember { FocusRequester() }

	var isLoading by remember { mutableStateOf(false) }
	var errorMessage by remember { mutableStateOf<String?>(null) }

	val authViewModel: AuthViewModel = viewModel()

	val colors = LocalColors.current

	val focusManager = LocalFocusManager.current
	val latestFocusManager by rememberUpdatedState(focusManager)

	val coroutineScope = rememberCoroutineScope()
	val keyboardController = LocalSoftwareKeyboardController.current

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
		modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).pointerInput(Unit) {
			detectTapGestures {
				latestFocusManager.clearFocus()
			}
		},
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(id = R.drawable.login),
			contentDescription = "Login",
			modifier = Modifier.fillMaxWidth().absoluteOffset(x = 0.dp, y = (-20).dp).zIndex(1f),
			contentScale = ContentScale.Fit
		)
		Column(
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Spacer(modifier = Modifier.weight(1f))
			Row(
				modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.Center
			) {
				Surface(
					modifier = Modifier
						.offset(y = 3.dp)
						.size(40.dp)
						.shadow(
							elevation = 3.dp,
							shape = CircleShape,
							ambientColor = Color.Black,
							spotColor = Color.Black
						),
					shape = CircleShape,
					color = Color.White
				) {
					IconButton(
						onClick = { navController.popBackStack() }
					) {
						Image(
							painter = painterResource(R.drawable.arrow_backward),
							contentDescription = "Back",
							modifier = Modifier.size(22.dp),
							colorFilter = ColorFilter.tint(colors.gradientTop)
						)
					}
				}
				Spacer(modifier = Modifier.weight(1f))
				Title("Connexion", 30)

				Spacer(modifier = Modifier.weight(1f))
			}
			Spacer(modifier = Modifier.height(40.dp))
			Input(
				value = email,
				placeholder = "Adresse e-mail",
				onValueChange = { email = it.lowercase() },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
				keyboardActions = KeyboardActions(
					onDone = {
						latestFocusManager.moveFocus(FocusDirection.Down)
					}
				),
				cursorColor = Color.White,
				modifier = Modifier.focusRequester(focusEmail),
				elevation = 0,
				focusedBorderColor = Color.White,
				unfocusedBorderColor = Color.White
			)
			Spacer(modifier = Modifier.height(16.dp))
			Input(
				value = password,
				placeholder = "Mot de passe",
				onValueChange = { password = it },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
				keyboardActions = KeyboardActions(
					onDone = {
						latestFocusManager.clearFocus()
					}
				),
				visualTransformation = PasswordVisualTransformation(),
				modifier = Modifier.focusRequester(focusPassword),
				elevation = 0,
				focusedBorderColor = Color.White,
				unfocusedBorderColor = Color.White
			)
			Spacer(modifier = Modifier.height(16.dp))
			Btn(
				text = "SE CONNECTER",
				modifier = Modifier.fillMaxWidth().height(45.dp),
				backgroundColor = Color.White,
				textColor = colors.primary,
				paddingVertical = 0,
				loading = isLoading,
				onClick = {
					keyboardController?.hide()
					isLoading = true
					errorMessage = null
					authViewModel.setAuthStatus(AuthModel.Authenticating)

					if (email.isEmpty() || password.isEmpty()) {
						coroutineScope.launch {
							snackbarHostState.showSnackbar(
								message = "Veuillez remplir tous les champs.",
								duration = SnackbarDuration.Short
							)
						}

						isLoading = false
						return@Btn
					}

					authViewModel.login(email, password) { success, error ->
						if (success) {
							isLoading = true
						} else {
							isLoading = false

							if (error != null) {
								errorMessage = "Erreur : $error"
								authViewModel.setAuthStatus(AuthModel.AuthenticationFailed(error))
							}
						}
					}
				}
			)
			Spacer(modifier = Modifier.weight(2f))
			Btn(
				onClick = {
					navController.navigate(ScreenModel.Signup.route) {
						popUpTo(ScreenModel.Signup.route) {
							inclusive	= true
						}
					}
				},
				modifier = Modifier.fillMaxWidth().padding(top = 85.dp, bottom = 12.dp).navigationBarsPadding().border(2.dp, color = Color.White, shape = RoundedCornerShape(30.dp)),
				backgroundColor = Color.Transparent,
				textColor = Color.White,
				textSize = 14.sp,
				paddingVertical = 0,
				text = "INSCRIVEZ-VOUS"
			)
		}
	}
}