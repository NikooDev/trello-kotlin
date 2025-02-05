package com.mydigitalschool.nicotrello.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mydigitalschool.nicotrello.data.model.ProjectModel
import com.mydigitalschool.nicotrello.data.model.toProjectModel
import com.mydigitalschool.nicotrello.services.FirebaseService
import kotlinx.coroutines.tasks.await

class ProjectRepository {
	private val db: FirebaseFirestore = FirebaseService.db
	private val collectionRef = db.collection("projects")

	suspend fun getProjectById(uid: String): ProjectModel? {
		return try {
			val snapshot = collectionRef.document(uid).get().await()
			snapshot.toProjectModel()
		} catch (e: Exception) {
			Log.e("ProjectRepository", "Erreur lors de la récupération du projet : ${e.message}")
			null
		}
	}

	suspend fun getAllProjects(userUID: String): List<ProjectModel> {
		return try {
			val snapshot = collectionRef.whereEqualTo("userUID", userUID).get().await()
			snapshot.documents.mapNotNull { it.toProjectModel() }
		} catch (e: Exception) {
			Log.e("ProjectRepository", "Erreur lors de la récupération des projets : ${e.message}")
			emptyList()
		}
	}

	suspend fun addProject(project: ProjectModel): Boolean {
		return try {
			collectionRef.document(project.uid).set(project).await()
			true
		} catch (e: Exception) {
			Log.e("ProjectRepository", "Erreur lors de l'ajout : ${e.message}")
			false
		}
	}

	suspend fun updateProject(project: ProjectModel): Boolean {
		return try {
			collectionRef.document(project.uid).set(project).await()
			true
		} catch (e: Exception) {
			Log.e("ProjectRepository", "Erreur lors de la mise à jour : ${e.message}")
			false
		}
	}

	suspend fun deleteProject(uid: String): Boolean {
		return try {
			collectionRef.document(uid).delete().await()
			true
		} catch (e: Exception) {
			Log.e("ProjectRepository", "Erreur lors de la suppression : ${e.message}")
			false
		}
	}
}