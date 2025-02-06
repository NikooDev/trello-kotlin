package com.mydigitalschool.nicotrello.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TitleViewModel : ViewModel() {
	private val _title = MutableStateFlow("")
	val title: StateFlow<String> = _title


	fun setTitle(newTitle: String) {
		_title.value = newTitle
	}
}