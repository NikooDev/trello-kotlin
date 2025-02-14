package com.mydigitalschool.nicotrello.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mydigitalschool.nicotrello.data.model.UserModel
import com.mydigitalschool.nicotrello.data.model.toUserModel
import com.mydigitalschool.nicotrello.data.service.FirebaseService
import kotlinx.coroutines.tasks.await

/**
 * User Repository
 */
class UserRepository {
	private val _db: FirebaseFirestore = FirebaseService.db
	private val _collectionRef = _db.collection("users")

	/**
	 * Récupère un utilisateur par son UID
	 */
	suspend fun getUserById(userUID: String): UserModel {
		return try {
			val snapshot = _collectionRef.document(userUID).get().await()

			snapshot.toUserModel()
		} catch (e: Exception) {
			throw Exception("Erreur lors de la récupération de l'utilisateur", e)
		}
	}

	/**
	 * Ajoute un utilisateur à Firestore
	 */
	suspend fun addUser(user: UserModel): Void {
		return try {
			_collectionRef.document(user.uid).set(user).await()
		} catch (e: Exception) {
			throw Exception("Erreur lors de l'ajout de l'utilisateur", e)
		}
	}
}