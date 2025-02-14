package com.mydigitalschool.nicotrello.ui.task

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydigitalschool.nicotrello.data.model.ResultModel
import com.mydigitalschool.nicotrello.data.model.TaskModel
import com.mydigitalschool.nicotrello.data.repository.TaskRepository
import com.mydigitalschool.nicotrello.data.service.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TaskViewModel : ViewModel() {
	private val _repository = TaskRepository()
	private val _storage = FirebaseService.storage

	private val _tasks = MutableStateFlow<List<TaskModel>>(emptyList())
	val tasks: StateFlow<List<TaskModel>> = _tasks.asStateFlow()

	private val _task = MutableStateFlow<TaskModel?>(null)
	val task: StateFlow<TaskModel?> = _task.asStateFlow()

	private val _isLoading = MutableStateFlow(true)
	val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

	private val _error = MutableStateFlow<String?>(null)
	val error: StateFlow<String?> = _error.asStateFlow()

	fun getTasksByList(listUID: String) {
		viewModelScope.launch {
			_isLoading.update { true }
			when (val result = _repository.getTasksByList(listUID)) {
				is ResultModel.Success -> _tasks.value = result.data.filter { it.listUID == listUID }
				is ResultModel.Error -> _error.update { result.message }
			}
			_isLoading.update { false }
		}
	}

	fun getTaskById(taskUID: String) {
		viewModelScope.launch {
			when (val result = _repository.getTaskById(taskUID)) {
				is ResultModel.Success -> _task.value = result.data
				is ResultModel.Error -> _error.update { result.message }
			}
		}
	}

	private fun getFileExtension(uri: Uri, context: Context): String {
		val contentResolver = context.contentResolver
		val mimeType = contentResolver.getType(uri)

		return when (mimeType) {
			"image/png" -> "png"
			"image/jpeg", "image/jpg" -> "jpg"
			else -> "jpg"
		}
	}

	private suspend fun addTaskRequest(task: TaskModel, onComplete: (Boolean, String?) -> Unit) {
		when (val result = _repository.addTask(task)) {
			is ResultModel.Success -> {
				getTasksByList(task.listUID)
				onComplete(true, result.data)
			}
			is ResultModel.Error -> {
				_error.update { result.message }
				onComplete(false, null)
			}
		}
	}

	private suspend fun updateTaskRequest(task: TaskModel, onComplete: (Boolean, String?) -> Unit) {
		when (val result = _repository.updateTask(task)) {
			is ResultModel.Success -> {
				task.uid?.let { getTaskById(it) }
				onComplete(true, result.data)
			}
			is ResultModel.Error -> {
				_error.update { result.message }
				onComplete(false, null)
			}
		}
	}

	fun addTask(task: TaskModel, pictureURI: Uri?, context: Context, onComplete: (Boolean, String?) -> Unit) {
		viewModelScope.launch {
			val storage = _storage.reference

			if (pictureURI != null) {
				val extension = getFileExtension(pictureURI, context)
				val fileName = "${UUID.randomUUID()}.$extension"
				val pictureRef = storage.child("users/${task.userUID}/${fileName}")

				try {
					pictureRef.putFile(pictureURI).await()
					val imageUrl = pictureRef.downloadUrl.await().toString()
					val taskWithPicture = task.copy(picture = imageUrl)

					addTaskRequest(taskWithPicture, onComplete)
				} catch (e: Exception) {
					_error.update { e.message ?: "Erreur lors de l'upload" }
					onComplete(false, null)
				}
			} else {
				addTaskRequest(task, onComplete)
			}
		}
	}

	fun updateTask(task: TaskModel, taskUID: String, pictureURI: Uri?, oldPicture: String?, context: Context, onComplete: (Boolean, String?) -> Unit) {
		viewModelScope.launch {
			task.uid = taskUID

			val storage = _storage.reference

			if (!oldPicture.isNullOrEmpty() && pictureURI != null) {
				try {
					val oldPictureRef = _storage.getReferenceFromUrl(oldPicture)
					oldPictureRef.delete().await()
				} catch (e: Exception) {
					_error.update { "Erreur suppression ancienne image: ${e.message}" }
					onComplete(false, null)
					return@launch
				}
			}

			if (pictureURI != null) {
				val extension = getFileExtension(pictureURI, context)
				val fileName = "${UUID.randomUUID()}.$extension"
				val newPictureRef = storage.child("users/${task.userUID}/$fileName")

				Log.d("TaskViewModel", "New picture URI: $pictureURI")

				try {
					newPictureRef.putFile(pictureURI).await()
					val imageUrl = newPictureRef.downloadUrl.await().toString()
					val updatedTask = task.copy(picture = imageUrl)

					updateTaskRequest(updatedTask, onComplete)
				} catch (e: Exception) {
					_error.update { e.message ?: "Erreur lors de l'upload" }
					onComplete(false, null)
				}
			} else {
				updateTaskRequest(task, onComplete)
			}
		}
	}

	fun deleteTask(task: TaskModel, listUID: String, onComplete: (Boolean, String?) -> Unit) {
		val taskUID = task.uid

		viewModelScope.launch {
			if (taskUID != null) {
				try {
					if (!task.picture.isNullOrEmpty()) {
						val storageRef = _storage.reference
						val imageRef = storageRef.storage.getReferenceFromUrl(task.picture)

						imageRef.delete().await()
					}

					when (val result = _repository.deleteTask(taskUID)) {
						is ResultModel.Success -> {
							getTasksByList(listUID)
							onComplete(true, result.data)
						}
						is ResultModel.Error -> {
							_error.update { result.message }
							onComplete(false, null)
						}
					}
				} catch (e: Exception) {
					_error.update { e.message ?: "Erreur lors de la suppression de l'image" }
					onComplete(false, null)
				}
			}
		}
	}
}