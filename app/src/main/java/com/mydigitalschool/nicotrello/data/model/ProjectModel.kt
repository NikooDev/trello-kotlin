package com.mydigitalschool.nicotrello.data.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.mydigitalschool.nicotrello.utils.toFormattedString
import com.mydigitalschool.nicotrello.utils.toIsoFormat

data class ProjectModel (
	val uid: String = "",
	val title: String = "",
	val author: String = "",
	val userUID: String = "",
	val nbTasks: Int = 0,
	val nbTasksEnd: Int = 0,
	val created: String? = null
){
	constructor(
		uid: String,
		title: String,
		author: String,
		userUID: String,
		nbTasks: Int,
		nbTasksEnd: Int,
		created: Timestamp?
	) : this(uid, title, author, userUID, nbTasks, nbTasksEnd, created?.toDate()?.toIsoFormat())
}

fun DocumentSnapshot.toProjectModel(): ProjectModel? {
	try {
		val uid = getString("uid") ?: ""
		val title = getString("title") ?: ""
		val author = getString("author") ?: ""
		val userUID = getString("userUID") ?: ""
		val nbTasks = getLong("nbTasks")?.toInt() ?: 0
		val nbTasksEnd = getLong("nbTasksEnd")?.toInt() ?: 0
		val createdTimestamp = getTimestamp("created")

		return ProjectModel(uid, title, author, userUID, nbTasks, nbTasksEnd, createdTimestamp?.toFormattedString())
	} catch (e: Exception) {
		Log.e("DocumentSnapshot", "Error converting to ProjectModel: ${e.message}")
		return null
	}
}