package com.mydigitalschool.nicotrello.manager

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.mydigitalschool.nicotrello.data.model.UserModel
import com.mydigitalschool.nicotrello.services.FirebaseService
import com.mydigitalschool.nicotrello.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Classe de gestion de l'authentification
 */
class AuthManager {
	private val auth: FirebaseAuth = FirebaseService.auth
	private val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
		val user = firebaseAuth.currentUser
		userLiveData.postValue(user)
	}
	private val userViewModel = UserViewModel()

	val userLiveData: MutableLiveData<FirebaseUser?> = MutableLiveData()

	fun addAuthStateListener() {
		auth.addAuthStateListener(authStateListener)
	}

	/**
	 * Inscription -> Email / Password
	 * Envoi email confirmation
	 */
	fun signup(email: String, password: String, firstname: String, lastname: String, onComplete: (Boolean, String?) -> Unit) {
		auth.createUserWithEmailAndPassword(email, password)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					val user = auth.currentUser

					user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
						if (verifyTask.isSuccessful) {
							val newUser = UserModel(
								uid = user.uid,
								email = email,
								firstname = firstname,
								lastname = lastname
							)
							CoroutineScope(Dispatchers.IO).launch {
								userViewModel.saveUser(newUser)
							}
							onComplete(true, null)
						} else {
							onComplete(false, verifyTask.exception?.message)
						}
					}
				} else {
					onComplete(false, task.exception?.message)
				}
			}
	}

	/**
	 * Connexion -> Email / Password
	 */
	fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
		auth.signInWithEmailAndPassword(email, password)
			.addOnCompleteListener { task ->
				if (task.isSuccessful) {
					onComplete(true, null)
				} else {
					val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
					val errorMessage: String

					when (errorCode) {
						"ERROR_INVALID_EMAIL" -> {
							errorMessage = "Adresse e-mail invalide."
						}
						"ERROR_WRONG_PASSWORD" -> {
							errorMessage = "Mot de passe incorrect."
						}
						"ERROR_USER_NOT_FOUND" -> {
							errorMessage = "Aucun compte trouvé."
						}
						"ERROR_INVALID_CREDENTIAL" -> {
							errorMessage = "Identifiants incorrects."
						}
						else -> {
							errorMessage = "Une erreur s'est produite lors de la connexion."
						}
					}

					onComplete(false, errorMessage)
				}
			}
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

	fun removeAuthStateListener() {
		auth.removeAuthStateListener(authStateListener)
	}
}