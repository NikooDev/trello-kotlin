package com.mydigitalschool.nicotrello.data.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class ProjectModel (
	var uid: String? = null,
	val title: String = "",
	val author: String = "",
	val userUID: String = "",
	val nbTasks: Int = 0,
	val created: Date? = null
){
	constructor(
		uid: String,
		title: String,
		author: String,
		userUID: String,
		nbTasks: Int,
		created: Timestamp?
	) : this(uid, title, author, userUID, nbTasks, created?.toDate())

	/**
	 * Companion objet
	 * Permet de définir la date de création lors de la création d'un projet
	 */
	companion object {
		fun create(
			title: String,
			author: String,
			userUID: String,
			nbTasks: Int = 0,
			created: Timestamp = Timestamp.now()
		): ProjectModel {
			val formattedDate = created.toDate()
			return ProjectModel(null, title, author, userUID, nbTasks, formattedDate)
		}
	}
}

/**
 * Serialization model
 * Permet de retourner les données de Firestore conforme au model
 */
fun DocumentSnapshot.toProjectModel(): ProjectModel? {
	try {
		val uid = id
		val title = getString("title") ?: ""
		val author = getString("author") ?: ""
		val userUID = getString("userUID") ?: ""
		val nbTasks = getLong("nbTasks")?.toInt() ?: 0
		val createdTimestamp = getTimestamp("created")

		return ProjectModel(uid, title, author, userUID, nbTasks, createdTimestamp)
	} catch (e: Exception) {
		Log.e("DocumentSnapshot", "Error converting to ProjectModel: ${e.message}")
		return null
	}
}