package com.mydigitalschool.nicotrello.data.service

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * FirebaseService Singleton
 * Ne s'exécute qu'une seule fois au lancement de l'application (déclaré dans AndroidManifest.xml)
 * Met à disposition les 3 services de Firebase
 *
 * Compagnion object permet de définir auth, db, storage avec l'équivalent des propriétés statiques
 * By lazy permet de différer les instances lorsqu'on en a besoin
 */
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