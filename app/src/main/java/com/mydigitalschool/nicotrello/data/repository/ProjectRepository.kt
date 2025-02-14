package com.mydigitalschool.nicotrello.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mydigitalschool.nicotrello.data.model.ProjectModel
import com.mydigitalschool.nicotrello.data.model.ResultModel
import com.mydigitalschool.nicotrello.data.model.toProjectModel
import com.mydigitalschool.nicotrello.data.service.FirebaseService
import kotlinx.coroutines.tasks.await

/**
 * Project Repository
 */
class ProjectRepository {
	private val _db: FirebaseFirestore = FirebaseService.db
	private val _collectionRef = _db.collection("projects")

	suspend fun getProjectsByUser(userUID: String): ResultModel<List<ProjectModel>> {
		return try {
			val snapshot = _collectionRef.whereEqualTo("userUID", userUID).orderBy("created", Query.Direction.DESCENDING).get().await()
			val projects = snapshot.documents.mapNotNull { it.toProjectModel() }

			ResultModel.Success(projects)
		} catch (e: Exception) {
			handleError("Erreur lors de la récupération des projets.", e)
		}
	}

	suspend fun getProjectById(projectUID: String): ResultModel<ProjectModel?> {
		return try {
			val snapshot = _collectionRef.document(projectUID).get().await()

			ResultModel.Success(snapshot.toProjectModel())
		} catch (e: Exception) {
			handleError("Erreur lors de la récupération du projet.", e)
		}
	}

	suspend fun getCountTasksByProjet(projectUID: String): ResultModel<Int> {
		return try {
			val tasks = _db.collection("tasks").whereEqualTo("projectUID", projectUID).get().await()

			ResultModel.Success(tasks.size())
		} catch (e: Exception) {
			handleError("Erreur lors de la récupération du nombre de tâches.", e)
		}
	}

	suspend fun addProject(project: ProjectModel): ResultModel<Pair<String, String>> {
		return try {
			val documentReference = _collectionRef.document()

			project.uid = documentReference.id
			documentReference.set(project).await()

			ResultModel.Success(Pair(documentReference.id, "Projet ajouté ! Redirection en cours..."))
		} catch (e: Exception) {
			handleError("Erreur lors de la création du projet.", e)
		}
	}

	suspend fun updateProject(project: ProjectModel): ResultModel<String> {
		return try {
			val uid = project.uid ?: throw Exception("L'UID du projet ne peut pas être nul")

			_collectionRef.document(uid).set(project).await()

			ResultModel.Success("Projet mis à jour.")
		} catch (e: Exception) {
			handleError("Erreur lors de la mise à jour.", e)
		}
	}

	suspend fun deleteProject(uid: String): ResultModel<String> {
		return try {
			_collectionRef.document(uid).delete().await()

			ResultModel.Success("Projet supprimé.")
		} catch (e: Exception) {
			handleError("Erreur lors de la suppression.", e)
		}
	}

	private fun <T> handleError(message: String, e: Exception): ResultModel.Error<T> {
		Log.e("ProjectRepository", "$message : ${e.message}")

		@Suppress("UNCHECKED_CAST")
		return ResultModel.Error<Any>(e.message ?: message) as ResultModel.Error<T>
	}
}