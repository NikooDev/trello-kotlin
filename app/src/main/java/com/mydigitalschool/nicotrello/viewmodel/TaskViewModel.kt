package com.mydigitalschool.nicotrello.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydigitalschool.nicotrello.data.model.TaskModel
import com.mydigitalschool.nicotrello.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {
	private val repository = TaskRepository()

	private val _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
	val tasks: StateFlow<List<TaskModel>> = _tasks.asStateFlow()

	private val _task = MutableStateFlow<TaskModel?>(null)
	val task: StateFlow<TaskModel?> = _task.asStateFlow()

	private val _isLoading = MutableStateFlow(true)
	val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

	fun fetchTasks(userUID: String, projectUID: String) {
		_isLoading.value = true

		viewModelScope.launch {
			try {
				_tasks.value = repository.getAllTasksByUserAndList(userUID, projectUID)
			} finally {
				_isLoading.value = false
			}
		}
	}

	fun fetchTaskById(uid: String) {
		viewModelScope.launch {
			_task.value = repository.getTaskById(uid)
		}
	}

	fun addTask(task: TaskModel, onComplete: (Boolean) -> Unit) {
		viewModelScope.launch {
			val success = repository.addTask(task)
			onComplete(success)
		}
	}

	fun updateTask(task: TaskModel, onComplete: (Boolean) -> Unit) {
		viewModelScope.launch {
			val success = repository.updateTask(task)
			onComplete(success)
		}
	}

	fun deleteTask(uid: String, onComplete: (Boolean) -> Unit) {
		viewModelScope.launch {
			val success = repository.deleteTask(uid)
			onComplete(success)
		}
	}
}