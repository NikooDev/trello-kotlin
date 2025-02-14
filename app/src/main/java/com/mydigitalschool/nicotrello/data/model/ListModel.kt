package com.mydigitalschool.nicotrello.data.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class ListModel (
	var uid: String? = null,
	val title: String = "",
	val userUID: String = "",
	val projectUID: String = "",
	val created: Date? = null
){
	constructor(
		uid: String,
		title: String,
		userUID: String,
		projectUID: String,
		created: Timestamp?
	) : this(uid, title, userUID, projectUID, created?.toDate())

	/**
	 * Companion objet
	 * Permet de définir la date de création lors de la création d'une liste
	 */
	companion object {
		fun create(
			title: String,
			userUID: String,
			projectUID: String,
			created: Timestamp = Timestamp.now()
		): ListModel {
			val formattedDate = created.toDate()
			return ListModel(null, title, userUID, projectUID, formattedDate)
		}
	}
}


/**
 * Serialization model
 * Permet de retourner les données de Firestore conforme au model
 */
fun DocumentSnapshot.toListModel(): ListModel? {
	try {
		val uid = id
		val title = getString("title") ?: ""
		val userUID = getString("userUID") ?: ""
		val projectUID = getString("projectUID") ?: ""
		val createdTimestamp = getTimestamp("created")

		return ListModel(uid, title, userUID, projectUID, createdTimestamp)
	} catch (e: Exception) {
		Log.e("DocumentSnapshot", "Error converting to ListModel: ${e.message}")
		return null
	}
}