package com.mydigitalschool.nicotrello.core.ui.components

import android.app.Activity
import android.view.Window
import android.graphics.Color
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import com.mydigitalschool.nicotrello.core.utils.setStatusBarColor
import com.mydigitalschool.nicotrello.data.model.AuthModel
import com.mydigitalschool.nicotrello.ui.theme.LocalColors

/**
 * Composant de gestion de la StatusBar
 */
@Composable
fun getWindowFromContext(): Window? {
	val context = LocalContext.current
	return if (context is Activity) {
		context.window
	} else {
		null
	}
}

@Composable
fun StatusBar(isLoading: Boolean, authStatus: AuthModel) {
	val window = getWindowFromContext()
	val colors = LocalColors.current

	window?.let {
		SideEffect {
			Log.d("StatusBar", "isLoading: $isLoading")

			val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
			windowInsetsController.isAppearanceLightStatusBars = false

			when {
				isLoading -> {
					setStatusBarColor(window, Color.parseColor("#A0000000"))
				}

				authStatus is AuthModel.Authenticated -> {
					setStatusBarColor(window, colors.primary.toArgb())
				}

				else -> {
					setStatusBarColor(window, Color.parseColor("#A0000000"))
				}
			}
		}
	}
}