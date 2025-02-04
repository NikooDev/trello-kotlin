package com.mydigitalschool.nicotrello.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.mydigitalschool.nicotrello.data.model.UserModel
import com.mydigitalschool.nicotrello.data.model.toUserModel
import com.mydigitalschool.nicotrello.services.FirebaseService
import kotlinx.coroutines.tasks.await

class UserRepository {
	private val db: FirebaseFirestore = FirebaseService.db
	private var listenerRegistration: ListenerRegistration? = null

	suspend fun saveUser(user: UserModel) {
		db.collection("users")
			.document(user.uid)
			.set(user)
			.await()
	}

	suspend fun getUser(uid: String): UserModel? {
		val snapshot = db.collection("users").document(uid).get().await()

		return snapshot.toUserModel()
	}

	fun listenToUserChanges(uid: String, onUserChanged: (UserModel?) -> Unit) {
		listenerRegistration?.remove()

		listenerRegistration = db.collection("users")
			.document(uid)
			.addSnapshotListener { snapshot, error ->
				if (error != null) {
					Log.d("UserRepository", "Erreur lors de l'écoute des changements: ${error.message}")
					return@addSnapshotListener
				}

				val user = snapshot?.toUserModel()
				onUserChanged(user)
			}
	}

	fun removeListener() {
		listenerRegistration?.remove()
		listenerRegistration = null
	}
}