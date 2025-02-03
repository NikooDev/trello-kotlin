package com.mydigitalschool.nicotrello.services

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FirebaseService : Application() {
	override fun onCreate() {
		super.onCreate()

		FirebaseApp.initializeApp(this)
	}

	companion object {
		val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
		val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
		val storage: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
	}
}