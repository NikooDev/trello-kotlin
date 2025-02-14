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
import androidx.compose.foundation.layout.width
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
import com.mydigitalschool.nicotrello.data.model.UserModel
import com.mydigitalschool.nicotrello.ui.theme.LocalColors
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(navController: NavHostController, snackbarHostState: SnackbarHostState) {
	var firstname by remember { mutableStateOf("") }
	var lastname by remember { mutableStateOf("") }
	var email by remember { mutableStateOf("") }
	var password by remember { mutableStateOf("") }

	val focusFirstname = remember { FocusRequester() }
	val focusLastname = remember { FocusRequester() }
	val focusEmail = remember { FocusRequester() }
	val focusPassword = remember { FocusRequester() }

	var isLoading by remember { mutableStateOf(false) }
	var errorMessage by remember { mutableStateOf<String?>(null) }

	val authViewModel: AuthViewModel = viewModel()

	val colors = LocalColors.current

	val focusManager = LocalFocusManager.current
	val latestFocusManager by rememberUpdatedState(focusManager)

	val coroutineScope = rememberCoroutineScope()

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
		modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
			.pointerInput(Unit) {
				detectTapGestures {
					latestFocusManager.clearFocus()
				}
			},
		verticalArrangement = Arrangement.Top,
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(id = R.drawable.signup),
			contentDescription = "Login",
			modifier = Modifier.fillMaxWidth().absoluteOffset(x = 0.dp, y = (-20).dp).zIndex(1f),
			contentScale = ContentScale.Fit
		)
		Column(
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
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
				Title("Inscription", 30)

				Spacer(modifier = Modifier.weight(1f))
			}
			Spacer(modifier = Modifier.height(40.dp))
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.fillMaxWidth()
			) {
				Input(
					value = firstname,
					placeholder = "Prénom",
					onValueChange = { firstname = it },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
					keyboardActions = KeyboardActions(
						onDone = {
							latestFocusManager.moveFocus(FocusDirection.Next)
						}
					),
					enabled = !isLoading,
					cursorColor = Color.White,
					modifier = Modifier.focusRequester(focusFirstname).weight(1f),
					elevation = 0,
					focusedBorderColor = Color.White,
					unfocusedBorderColor = Color.White
				)
				Spacer(modifier = Modifier.width(16.dp))
				Input(
					value = lastname,
					placeholder = "Nom",
					onValueChange = { lastname = it },
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
					keyboardActions = KeyboardActions(
						onDone = {
							latestFocusManager.moveFocus(FocusDirection.Down)
						}
					),
					enabled = !isLoading,
					cursorColor = Color.White,
					modifier = Modifier.focusRequester(focusLastname).weight(1f),
					elevation = 0,
					focusedBorderColor = Color.White,
					unfocusedBorderColor = Color.White
				)
			}
			Spacer(modifier = Modifier.height(16.dp))
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
				enabled = !isLoading,
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
				onValueChange = { password = it.trim() },
				keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
				keyboardActions = KeyboardActions(
					onDone = {
						latestFocusManager.clearFocus()
					}
				),
				enabled = !isLoading,
				visualTransformation = PasswordVisualTransformation(),
				modifier = Modifier.focusRequester(focusPassword),
				elevation = 0,
				focusedBorderColor = Color.White,
				unfocusedBorderColor = Color.White
			)
			Spacer(modifier = Modifier.height(16.dp))
			Btn(
				text = "S'INSCRIRE",
				modifier = Modifier.fillMaxWidth().height(45.dp),
				backgroundColor = Color.White,
				textColor = colors.primary,
				paddingVertical = 0,
				loading = isLoading,
				enabled = !isLoading,
				onClick = {
					isLoading = true

					if (firstname.trim().isNotEmpty() && lastname.trim().isNotEmpty() && email.trim().isNotEmpty() && password.trim().isNotEmpty()) {
						val user = UserModel.from(
							uid = "",
							email = email,
							firstname = firstname,
							lastname = lastname
						)

						authViewModel.signup(user, password) { success, error ->
							if (success) {
								isLoading = true

								coroutineScope.launch {
									snackbarHostState.showSnackbar(
										message = "Inscription réussie !\nUn e-mail de confirmation vous a été envoyé.",
										duration = SnackbarDuration.Short
									)

									navController.navigate(ScreenModel.Login.route)
								}
							} else {
								isLoading = false

								if (error != null) {
									errorMessage = "Erreur : $error"
									authViewModel.setAuthStatus(AuthModel.AuthenticationFailed(error))
								}
							}
						}
					} else {
						coroutineScope.launch {
							snackbarHostState.showSnackbar(
								message = "Veuillez remplir tous les champs.",
								duration = SnackbarDuration.Short
							)
						}

						isLoading = false
					}
				}
			)
			Spacer(modifier = Modifier.weight(1f))
			Btn(
				onClick = {
					navController.navigate(ScreenModel.Login.route) {
						popUpTo(ScreenModel.Login.route) {
							inclusive = true
						}
					}
				},
				modifier = Modifier.fillMaxWidth().padding(top = 100.dp, bottom = 12.dp)
					.navigationBarsPadding()
					.border(2.dp, color = Color.White, shape = RoundedCornerShape(30.dp)),
				backgroundColor = Color.Transparent,
				textColor = Color.White,
				textSize = 14.sp,
				paddingVertical = 0,
				text = "J'AI DÉJÀ UN COMPTE"
			)
		}
	}
}