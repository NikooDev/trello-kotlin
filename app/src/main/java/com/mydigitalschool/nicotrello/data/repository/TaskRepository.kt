package com.mydigitalschool.nicotrello.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mydigitalschool.nicotrello.data.model.ResultModel
import com.mydigitalschool.nicotrello.data.model.TaskModel
import com.mydigitalschool.nicotrello.data.model.toTaskModel
import com.mydigitalschool.nicotrello.data.service.FirebaseService
import kotlinx.coroutines.tasks.await

/**
 * Task Repository
 */
class TaskRepository {
	private val _db: FirebaseFirestore = FirebaseService.db
	private val _collectionRef = _db.collection("tasks")

	suspend fun getTasksByList(listUID: String): ResultModel<List<TaskModel>> {
		return try {
			val snapshot = _collectionRef.whereEqualTo("listUID", listUID).orderBy("created", Query.Direction.DESCENDING).get().await()
			val tasks = snapshot.documents.mapNotNull { it.toTaskModel() }

			ResultModel.Success(tasks)
		} catch (e: Exception) {
			handleError("Erreur lors de la récupération des tâches.", e)
		}
	}

	suspend fun getTaskById(taskUID: String): ResultModel<TaskModel?> {
		return try {
			val snapshot = _collectionRef.document(taskUID).get().await()

			ResultModel.Success(snapshot.toTaskModel())
		} catch (e: Exception) {
			handleError("Erreur lors de la récupération de la tâche.", e)
		}
	}

	suspend fun addTask(task: TaskModel): ResultModel<String> {
		return try {
			val documentReference = _collectionRef.document()

			task.uid = documentReference.id
			documentReference.set(task).await()

			ResultModel.Success("Tâche ajoutée.")
		} catch (e: Exception) {
			handleError("Erreur lors de l'ajout.", e)
		}
	}

	suspend fun updateTask(task: TaskModel): ResultModel<String> {
		return try {
			val uid = task.uid ?: throw Exception("L'UID de la tâche ne peut pas être nul")

			_collectionRef.document(uid).set(task).await()

			ResultModel.Success("Tâche mise à jour.")
		} catch (e: Exception) {
			handleError("Erreur lors de la mise à jour.", e)
		}
	}

	suspend fun deleteTask(uid: String): ResultModel<String> {
		return try {
			_collectionRef.document(uid).delete().await()

			ResultModel.Success("Tâche supprimée.")
		} catch (e: Exception) {
			handleError("Erreur lors de la suppression.", e)
		}
	}

	private fun <T> handleError(message: String, e: Exception): ResultModel.Error<T> {
		Log.e("TaskRepository", "$message : ${e.message}")

		@Suppress("UNCHECKED_CAST")
		return ResultModel.Error<Any>(e.message ?: message) as ResultModel.Error<T>
	}
}