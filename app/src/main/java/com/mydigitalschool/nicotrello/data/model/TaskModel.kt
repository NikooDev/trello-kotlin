package com.mydigitalschool.nicotrello.data.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

enum class Priority {
	LOW, MEDIUM, HIGH;

	companion object {
		fun fromString(value: String?): Priority? {
			return when (value) {
				"LOW" -> LOW
				"MEDIUM" -> MEDIUM
				"HIGH" -> HIGH
				else -> null
			}
		}

		fun toString(value: Priority?): String? {
			return value?.name
		}
	}
}

data class TaskModel (
	var uid: String? = null,
	val userUID: String = "",
	val projectUID: String = "",
	val listUID: String = "",
	val author: String = "",
	val title: String = "",
	val description: String = "",
	val priority: Priority? = null,
	val picture: String? = null,
	val created: Date? = null
) {
	constructor(
		uid: String,
		userUID: String,
		projectUID: String,
		listUID: String,
		author: String,
		title: String,
		description: String,
		priority: Priority?,
		picture: String?,
		created: Timestamp?
	) : this(uid, userUID, projectUID, listUID, author, title, description, priority, picture, created?.toDate())

	/**
	 * Companion objet
	 * Permet de définir la date de création lors de la création d'une tâche
	 */
	companion object {
		fun create(
			userUID: String,
			projectUID: String,
			listUID: String,
			author: String,
			title: String,
			description: String,
			priority: Priority?,
			picture: String?,
			created: Timestamp = Timestamp.now()
		): TaskModel {
			val formattedDate = created.toDate()
			return TaskModel(null, userUID, projectUID, listUID, author, title, description, priority, picture, formattedDate)
		}
	}
}

/**
 * Serialization model
 * Permet de retourner les données de Firestore conforme au model
 */
fun DocumentSnapshot.toTaskModel(): TaskModel? {
	try {
		val uid = id
		val userUID = getString("userUID") ?: ""
		val projectUID = getString("projectUID") ?: ""
		val listUID = getString("listUID") ?: ""
		val author = getString("author") ?: ""
		val title = getString("title") ?: ""
		val description = getString("description") ?: ""
		val priorityString = getString("priority") ?: ""
		val priority: Priority? = Priority.fromString(priorityString)
		val picture = getString("picture")
		val createdTimestamp = getTimestamp("created")

		return TaskModel(uid, userUID, projectUID, listUID, author, title, description, priority, picture, createdTimestamp)
	} catch (e: Exception) {
		Log.e("DocumentSnapshot", "Error converting to TaskModel: ${e.message}")
		return null
	}
}