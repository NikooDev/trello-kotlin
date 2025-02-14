package com.mydigitalschool.nicotrello.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.util.Date

data class UserModel(
	val uid: String = "",
	val email: String = "",
	val firstname: String = "",
	val lastname: String = "",
	val created: Date? = null
) {
	constructor(
		uid: String,
		email: String,
		firstname: String,
		lastname: String,
		created: Timestamp?
	) : this(uid, email, firstname, lastname, created?.toDate())

	companion object {
		fun from(
			uid: String,
			email: String,
			firstname: String,
			lastname: String,
			created: Timestamp = Timestamp.now()
		): UserModel {
			val formattedDate = created.toDate()
			return UserModel(uid, email.trim().lowercase(), firstname.trim().lowercase(), lastname.trim().lowercase(), formattedDate)
		}
	}
}

fun DocumentSnapshot.toUserModel(): UserModel {
	try {
		val uid = id
		val email = getString("email") ?: ""
		val firstname = getString("firstname") ?: ""
		val lastname = getString("lastname") ?: ""
		val createdTimestamp = getTimestamp("created")

		return UserModel(uid, email, firstname, lastname, createdTimestamp)
	} catch (e: Exception) {
		throw Exception("Erreur de sérialisation UserModel", e)
	}
}