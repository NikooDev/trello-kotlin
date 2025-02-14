package com.mydigitalschool.nicotrello.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydigitalschool.nicotrello.data.model.ListModel
import com.mydigitalschool.nicotrello.data.model.ResultModel
import com.mydigitalschool.nicotrello.data.repository.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListViewModel : ViewModel() {
	private val _repository = ListRepository()

	private val _lists = MutableStateFlow<List<ListModel>>(emptyList())
	val lists: StateFlow<List<ListModel>> = _lists.asStateFlow()

	private val _list = MutableStateFlow<ListModel?>(null)
	val list: StateFlow<ListModel?> = _list.asStateFlow()

	private val _isLoading = MutableStateFlow(true)
	val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

	private val _error = MutableStateFlow<String?>(null)
	val error: StateFlow<String?> = _error.asStateFlow()

	private val _scrollTop = MutableStateFlow(false)
	val scrollTop: StateFlow<Boolean> = _scrollTop.asStateFlow()

	private val _currentPage = MutableStateFlow(0)
	val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

	fun getListsByProjet(projectUID: String) {
		viewModelScope.launch {
			_isLoading.update { true }
			when (val result = _repository.getListsByProject(projectUID)) {
				is ResultModel.Success -> _lists.value = result.data
				is ResultModel.Error -> _error.update { result.message }
			}
			_isLoading.update { false }
		}
	}

	fun getListById(listUID: String) {
		viewModelScope.launch {
			when (val result = _repository.getListById(listUID)) {
				is ResultModel.Success -> _list.value = result.data
				is ResultModel.Error -> _error.update { result.message }
			}
		}
	}

	fun addList(list: ListModel, onComplete: (Boolean, String?) -> Unit) {
		viewModelScope.launch {
			when (val result = _repository.addList(list)) {
				is ResultModel.Success -> {
					getListsByProjet(list.projectUID)
					onComplete(true, result.data)
				}
				is ResultModel.Error -> {
					_error.update { result.message }
					onComplete(false, null)
				}
			}
		}
	}

	fun updateList(list: ListModel, listUID: String, onComplete: (Boolean, String?) -> Unit) {
		viewModelScope.launch {
			list.uid = listUID
			when (val result = _repository.updateList(list)) {
				is ResultModel.Success -> {
					val currentLists = _lists.value.toMutableList()
					val index = currentLists.indexOfFirst { it.uid == list.uid }

					if (index != -1) {
						currentLists[index] = list
						_lists.update { currentLists.toList() }
					}
					onComplete(true, result.data)
				}
				is ResultModel.Error -> {
					_error.update { result.message }
					onComplete(false, null)
				}
			}
		}
	}

	fun deleteList(uid: String, onComplete: (Boolean, String?) -> Unit) {
		viewModelScope.launch {
			when (val result = _repository.deleteList(uid)) {
				is ResultModel.Success -> {
					val currentLists = _lists.value.toMutableList()
					val index = currentLists.indexOfFirst { it.uid == uid }

					if (index != -1) {
						currentLists.removeAt(index)
						_lists.update { currentLists.toList() }
					}
					onComplete(true, result.data)
				}
				is ResultModel.Error -> {
					_error.update { result.message }
					onComplete(false, null)
				}
			}
		}
	}

	fun setCurrentPage(page: Int) {
		_currentPage.update { page }
	}

	fun setScrollTop(value: Boolean) {
		_scrollTop.update { value }
	}
}