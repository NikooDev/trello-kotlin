package com.mydigitalschool.nicotrello.data.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import com.mydigitalschool.nicotrello.utils.toIsoFormat

data class UserModel(
	val uid: String = "",
	val email: String = "",
	val firstname: String = "",
	val lastname: String = "",
	val created: String? = null
) {
	constructor(
		uid: String,
		email: String,
		firstname: String,
		lastname: String,
		created: Timestamp?
	) : this(uid, email, firstname, lastname, created?.toDate()?.toIsoFormat())
}

fun DocumentSnapshot.toUserModel(): UserModel? {
	try {
		val uid = getString("uid") ?: ""
		val email = getString("email") ?: ""
		val firstname = getString("firstname") ?: ""
		val lastname = getString("lastname") ?: ""
		val createdTimestamp = getTimestamp("created")

		return UserModel(uid, email, firstname, lastname, createdTimestamp)
	} catch (e: Exception) {
		Log.e("DocumentSnapshot", "Error converting to UserModel: ${e.message}")
		return null
	}
}