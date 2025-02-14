package com.mydigitalschool.nicotrello.ui.app

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AppViewModel : ViewModel() {
	/**
	 * Titre de la TopBar
	 */
	private val _title = MutableStateFlow("")
	val title: StateFlow<String> = _title.asStateFlow()

	/**
	 * Loader sur routes authentifiées
	 */
	private val _appLoading = MutableStateFlow(false)
	val appLoading: StateFlow<Boolean> = _appLoading.asStateFlow()

	fun setTitle(newTitle: String) {
		_title.update { newTitle }
	}

	fun setAppLoading(loading: Boolean) {
		_appLoading.update { loading }
	}
}