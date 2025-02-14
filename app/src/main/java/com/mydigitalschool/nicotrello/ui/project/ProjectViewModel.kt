package com.mydigitalschool.nicotrello.ui.project

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydigitalschool.nicotrello.data.model.ProjectModel
import com.mydigitalschool.nicotrello.data.model.ResultModel
import com.mydigitalschool.nicotrello.data.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProjectViewModel : ViewModel() {
	private val _repository = ProjectRepository()

	private val _projects = MutableStateFlow<List<ProjectModel>>(emptyList())
	val projects: StateFlow<List<ProjectModel>> = _projects.asStateFlow()

	private val _project = MutableStateFlow<ProjectModel?>(null)
	val project: StateFlow<ProjectModel?> = _project.asStateFlow()

	private val _isLoading = MutableStateFlow(true)
	val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

	private val _error = MutableStateFlow<String?>(null)
	val error: StateFlow<String?> = _error.asStateFlow()

	fun getProjectsByUser(userUID: String) {
		viewModelScope.launch {
			_isLoading.update { true }
			when (val result = _repository.getProjectsByUser(userUID)) {
				is ResultModel.Success -> _projects.value = result.data
				is ResultModel.Error -> _error.update { result.message }
			}
			_isLoading.update { false }
		}
	}

	fun getProjectById(projectUID: String) {
		viewModelScope.launch {
			when (val result = _repository.getProjectById(projectUID)) {
				is ResultModel.Success -> _project.value = result.data
				is ResultModel.Error -> _error.update { result.message }
			}
		}
	}

	/**
	 * Récupère le nombre de tâches d'un projet
	 * Si le nombre de tâches est différent du nombre de tâches du projet :
	 * -> Met à jour le document Firestore du projet
	 * -> Met à jour le state local des projets
	 */
	fun getCountTasksByProjet(projectUID: String) {
		viewModelScope.launch {
			when (val result = _repository.getCountTasksByProjet(projectUID)) {
				is ResultModel.Success -> {
					val currentProjects = _projects.value.toMutableList()
					val index = currentProjects.indexOfFirst { it.uid == projectUID }

					if (index != -1) {
						val currentProject = currentProjects[index]

						if (currentProject.nbTasks != result.data) {
							val updatedProject = currentProject.copy(nbTasks = result.data)
							currentProjects[index] = updatedProject

							_repository.updateProject(updatedProject)
							_projects.update { currentProjects.toList() }
						}
					}
				}
				is ResultModel.Error -> {
					_error.update { result.message }
				}
			}
		}
	}

	fun addProject(project: ProjectModel, onComplete: (Boolean, String?, String?) -> Unit) {
		viewModelScope.launch {
			when (val result = _repository.addProject(project)) {
				is ResultModel.Success -> {
					getProjectsByUser(project.userUID)
					val (projectUID, message) = result.data
					onComplete(true, projectUID, message)
				}
				is ResultModel.Error -> {
					_error.update { result.message }
					onComplete(false, null, null)
				}
			}
		}
	}

	fun updateProject(project: ProjectModel, projectUID: String, onComplete: (Boolean, String?) -> Unit) {
		viewModelScope.launch {
			project.uid = projectUID
			when (val result = _repository.updateProject(project)) {
				is ResultModel.Success -> {
					val currentProjects = _projects.value.toMutableList()
					val index = currentProjects.indexOfFirst { it.uid == project.uid }

					if (index != -1) {
						currentProjects[index] = project
						_projects.update { currentProjects.toList() }
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

	fun deleteProject(uid: String, onComplete: (Boolean, String?) -> Unit) {
		viewModelScope.launch {
			when (val result = _repository.deleteProject(uid)) {
				is ResultModel.Success -> {
					val currentProjects = _projects.value.toMutableList()
					val index = currentProjects.indexOfFirst { it.uid == uid }

					if (index != -1) {
						currentProjects.removeAt(index)
						_projects.update { currentProjects.toList() }
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
}