package com.mydigitalschool.nicotrello.core.utils

import android.os.Build
import android.view.Window
import android.view.WindowInsets

fun setStatusBarColor(window: Window, color: Int) {
	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
		window.decorView.setOnApplyWindowInsetsListener { view, insets ->
			val statusBarInsets = insets.getInsets(WindowInsets.Type.statusBars())
			view.setBackgroundColor(color)

			view.setPadding(0, statusBarInsets.top, 0, 0)
			insets
		}
	} else {
		window.statusBarColor = color
	}
}