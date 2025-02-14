package com.mydigitalschool.nicotrello.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mydigitalschool.nicotrello.data.model.ListModel
import com.mydigitalschool.nicotrello.data.model.ResultModel
import com.mydigitalschool.nicotrello.data.model.toListModel
import com.mydigitalschool.nicotrello.data.service.FirebaseService
import kotlinx.coroutines.tasks.await

/**
 * List Repository
 */
class ListRepository {
	private val _db: FirebaseFirestore = FirebaseService.db
	private val _collectionRef = _db.collection("lists")

	suspend fun getListsByProject(projectUID: String): ResultModel<List<ListModel>> {
		return try {
			val snapshot = _collectionRef.whereEqualTo("projectUID", projectUID).orderBy("created", Query.Direction.DESCENDING).get().await()
			val lists = snapshot.documents.mapNotNull { it.toListModel() }

			ResultModel.Success(lists)
		} catch (e: Exception) {
			handleError("Erreur lors de la récupération des listes.", e)
		}
	}

	suspend fun getListById(listUID: String): ResultModel<ListModel?> {
		return try {
			val snapshot = _collectionRef.document(listUID).get().await()

			ResultModel.Success(snapshot.toListModel())
		} catch (e: Exception) {
			handleError("Erreur lors de la récupération de la liste.", e)
		}
	}

	suspend fun addList(list: ListModel): ResultModel<String> {
		return try {
			val documentReference = _collectionRef.document()

			list.uid = documentReference.id
			documentReference.set(list).await()

			ResultModel.Success("Liste ajoutée.")
		} catch (e: Exception) {
			handleError("Erreur lors de l'ajout.", e)
		}
	}

	suspend fun updateList(list: ListModel): ResultModel<String> {
		return try {
			val uid = list.uid ?: throw Exception("L'UID de la liste ne peut pas être nul")

			_collectionRef.document(uid).set(list).await()

			ResultModel.Success("Liste mise à jour.")
		} catch (e: Exception) {
			handleError("Erreur lors de la mise à jour.", e)
		}
	}

	suspend fun deleteList(uid: String): ResultModel<String> {
		return try {
			_collectionRef.document(uid).delete().await()

			ResultModel.Success("Liste supprimée.")
		} catch (e: Exception) {
			handleError("Erreur lors de la suppression.", e)
		}
	}

	private fun <T> handleError(message: String, e: Exception): ResultModel.Error<T> {
		Log.e("ListRepository", "$message : ${e.message}")

		@Suppress("UNCHECKED_CAST")
		return ResultModel.Error<Any>(e.message ?: message) as ResultModel.Error<T>
	}
}