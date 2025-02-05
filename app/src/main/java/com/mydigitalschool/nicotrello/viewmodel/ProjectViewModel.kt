package com.mydigitalschool.nicotrello.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydigitalschool.nicotrello.data.model.ProjectModel
import com.mydigitalschool.nicotrello.data.repository.ProjectRepository
import com.mydigitalschool.nicotrello.manager.AuthManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProjectViewModel : ViewModel() {
	private val repository = ProjectRepository()

	private val _projects = MutableStateFlow<List<ProjectModel>>(emptyList())
	val projects: StateFlow<List<ProjectModel>> = _projects.asStateFlow()

	private val _project = MutableStateFlow<ProjectModel?>(null)
	val project: StateFlow<ProjectModel?> = _project.asStateFlow()

	private val authManager = AuthManager()
	private val user = authManager.getCurrentUser()

	fun fetchProjects() {
		viewModelScope.launch {
			_projects.value = user?.let { repository.getAllProjects(it.uid) } ?: emptyList()
		}
	}

	fun fetchProjectById(uid: String) {
		viewModelScope.launch {
			_project.value = repository.getProjectById(uid)
		}
	}

	fun addProject(project: ProjectModel, onComplete: (Boolean) -> Unit) {
		viewModelScope.launch {
			val success = repository.addProject(project)
			onComplete(success)
			if (success) fetchProjects()
		}
	}

	fun updateProject(project: ProjectModel, onComplete: (Boolean) -> Unit) {
		viewModelScope.launch {
			val success = repository.updateProject(project)
			onComplete(success)
			if (success) fetchProjects()
		}
	}

	fun deleteProject(uid: String, onComplete: (Boolean) -> Unit) {
		viewModelScope.launch {
			val success = repository.deleteProject(uid)
			onComplete(success)
			if (success) fetchProjects()
		}
	}
}