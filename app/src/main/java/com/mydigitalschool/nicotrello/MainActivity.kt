package com.mydigitalschool.nicotrello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.mydigitalschool.nicotrello.ui.navigation.AppNavigation
import com.mydigitalschool.nicotrello.ui.theme.NicotrelloTheme

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			NicotrelloTheme {
				AppNavigation()
			}
		}
	}
}