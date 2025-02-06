package com.mydigitalschool.nicotrello.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mydigitalschool.nicotrello.data.model.TaskModel
import com.mydigitalschool.nicotrello.data.model.toTaskModel
import com.mydigitalschool.nicotrello.services.FirebaseService
import kotlinx.coroutines.tasks.await

class TaskRepository {
	private val db: FirebaseFirestore = FirebaseService.db
	private val collectionRef = db.collection("tasks")

	suspend fun getTaskById(uid: String): TaskModel? {
		return try {
			val snapshot = collectionRef.document(uid).get().await()
			snapshot.toTaskModel()
		} catch (e: Exception) {
			Log.e("TaskRepository", "Erreur lors de la récupération de la tâche : ${e.message}")
			null
		}
	}

	suspend fun getAllTasksByUserAndList(userUID: String, listUID: String): List<TaskModel> {
		return try {
			val snapshot = collectionRef.whereEqualTo("userUID", userUID).whereEqualTo("listUID", listUID).get().await()
			snapshot.documents.mapNotNull { it.toTaskModel() }
		} catch (e: Exception) {
			Log.e("TaskRepository", "Erreur lors de la récupération des tâches : ${e.message}")
			emptyList()
		}
	}

	suspend fun addTask(task: TaskModel): Boolean {
		return try {
			val documentReference = collectionRef.document()
			task.uid = documentReference.id
			documentReference.set(task).await()
			true
		} catch (e: Exception) {
			Log.e("TaskRepository", "Erreur lors de l'ajout : ${e.message}")
			false
		}
	}

	suspend fun updateTask(task: TaskModel): Boolean {
		return try {
			task.uid?.let {
				collectionRef.document(it).set(task).await()
				true
			} ?: false
		} catch (e: Exception) {
			Log.e("TaskRepository", "Erreur lors de la mise à jour : ${e.message}")
			false
		}
	}

	suspend fun deleteTask(uid: String): Boolean {
		return try {
			collectionRef.document(uid).delete().await()
			true
		} catch (e: Exception) {
			Log.e("TaskRepository", "Erreur lors de la suppression : ${e.message}")
			false
		}
	}
}