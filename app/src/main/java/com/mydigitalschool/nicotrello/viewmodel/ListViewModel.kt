package com.mydigitalschool.nicotrello.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydigitalschool.nicotrello.data.model.ListModel
import com.mydigitalschool.nicotrello.data.repository.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
	private val repository = ListRepository()

	private val _lists = MutableStateFlow<List<ListModel>>(emptyList())
	val lists: StateFlow<List<ListModel>> = _lists.asStateFlow()

	private val _list = MutableStateFlow<ListModel?>(null)
	val list: StateFlow<ListModel?> = _list.asStateFlow()

	private val _isLoading = MutableStateFlow(true)
	val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

	fun fetchLists(projectUID: String) {
		_isLoading.value = true

		viewModelScope.launch {
			try {
				_lists.value = repository.getListsByProject(projectUID)
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun fetchListById(uid: String) {
		viewModelScope.launch {
			_list.value = repository.getListById(uid)
		}
	}

	fun addList(list: ListModel, onComplete: (Boolean) -> Unit) {
		viewModelScope.launch {
			val success = repository.addList(list)
			onComplete(success)
		}
	}

	fun updateList(list: ListModel, onComplete: (Boolean) -> Unit) {
		viewModelScope.launch {
			val success = repository.updateList(list)
			onComplete(success)
		}
	}

	fun deleteList(uid: String, onComplete: (Boolean) -> Unit) {
		viewModelScope.launch {
			val success = repository.deleteList(uid)
			onComplete(success)
		}
	}
}