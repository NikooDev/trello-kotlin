package com.mydigitalschool.nicotrello.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.mydigitalschool.nicotrello.data.model.ListModel
import com.mydigitalschool.nicotrello.data.model.toListModel
import com.mydigitalschool.nicotrello.services.FirebaseService
import kotlinx.coroutines.tasks.await

class ListRepository {
	private val db: FirebaseFirestore = FirebaseService.db
	private val collectionRef = db.collection("lists")

	suspend fun getListById(uid: String): ListModel? {
		return try {
			val document = collectionRef.document(uid).get().await()
			document.toListModel()
		} catch (e: Exception) {
			Log.e("ListRepository", "Erreur lors de la récupération de la liste : ${e.message}")
			null
		}
	}

	suspend fun getListsByProject(projectUID: String): List<ListModel> {
		return try {
			val snapshot = collectionRef.whereEqualTo("projectUID", projectUID).get().await()
			snapshot.documents.mapNotNull { it.toListModel() }
		} catch (e: Exception) {
			Log.e("ListRepository", "Erreur lors de la récupération des lists : ${e.message}")
			emptyList()
		}
	}

	suspend fun addList(list: ListModel): Boolean {
		return try {
			val documentReference = collectionRef.document()
			list.uid = documentReference.id
			documentReference.set(list).await()
			true
		} catch (e: Exception) {
			Log.e("ListRepository", "Erreur lors de l'ajout : ${e.message}")
			false
		}
	}

	suspend fun updateList(list: ListModel): Boolean {
		return try {
			list.uid?.let {
				collectionRef.document(it).set(list).await()
				true
			} ?: false
		} catch (e: Exception) {
			Log.e("ListRepository", "Erreur lors de la mise à jour : ${e.message}")
			false
		}
	}

	suspend fun deleteList(uid: String): Boolean {
		return try {
			collectionRef.document(uid).delete().await()
			true
		} catch (e: Exception) {
			Log.e("ListRepository", "Erreur lors de la suppression : ${e.message}")
			false
		}
	}
}