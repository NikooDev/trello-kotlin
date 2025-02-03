package com.mydigitalschool.nicotrello.manager

import android.app.Activity
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mydigitalschool.nicotrello.services.FirebaseService

/**
 * Classe de gestion de l'authentification
 */
class AuthManager(private val activity: Activity, private val credentialManager: CredentialManager) {
	private val auth: FirebaseAuth = FirebaseService.auth

	/**
	 * Inscription classique -> Email / Password
	 */
	fun signup(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
		auth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener(activity) { task ->
				if (task.isSuccessful) {
					onComplete(true, null)
				} else {
					onComplete(false, task.exception?.message)
				}
			}
	}

	/**
	 * Connexion classique -> Email / Password
	 */
	fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
		auth
			.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener(activity) { task ->
				if (task.isSuccessful) {
					onComplete(true, null)
				} else {
					onComplete(false, task.exception?.message)
				}
		}
	}

	/**
	 * Connexion avec GoogleProvider
	 * -> Si aucun compte alors création automatique par Firebase
	 */
	fun loginWithGoogle(onComplete: (Boolean, String?) -> Unit) {

	}

	/**
	 * Callback à la réponse du Credential Manager
	 */
	private fun handleGoogleSignInResult(credential: Credential, onComplete: (Boolean, String?) -> Unit) {

	}

	/**
	 * Déconnexion
	 */
	fun logout() {
		auth.signOut()
	}

	/**
	 * Retourne l'utilisateur courant
	 */
	fun getCurrentUser(): FirebaseUser? = auth.currentUser
}