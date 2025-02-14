package com.mydigitalschool.nicotrello.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.mydigitalschool.nicotrello.data.model.AuthModel
import com.mydigitalschool.nicotrello.data.model.UserModel
import com.mydigitalschool.nicotrello.data.repository.UserRepository
import com.mydigitalschool.nicotrello.data.service.FirebaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel de l'authentification.
 */
class AuthViewModel : ViewModel() {
	private val _auth = FirebaseService.auth

	private val _repository = UserRepository()

	/**
	 * Statut de l'authentification
	 */
	private val _authStatus = MutableStateFlow<AuthModel>(AuthModel.Unauthenticated)
	val authStatus: StateFlow<AuthModel> = _authStatus.asStateFlow()

	/**
	 * Statut de l'écran de chargement
	 */
	private val _isLoading = MutableStateFlow(true)
	val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

	/**
	 * Utilisateur connecté
	 */
	private val _user = MutableStateFlow<UserModel?>(null)
	val user: StateFlow<UserModel?> = _user.asStateFlow()

	/**
	 * Listener d'authentification Firebase.
	 */
	private val _authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
		val firebaseUser = firebaseAuth.currentUser

		if (firebaseUser != null) {
			_isLoading.update { true }

			/**
			 * Lancer à tout moment du cycle de vie de l'application
			 */
			viewModelScope.launch {
				try {
					val user = _repository.getUserById(firebaseUser.uid)

					/**
					 * Vérifie si l'utilisateur à un compte dans Firestore et donc validé son email
					 */
					if (user.email.isNotEmpty() && firebaseUser.isEmailVerified) {
						_authStatus.update { AuthModel.Authenticated(user) }
					} else {
						logout()
					}
				} catch (e: Exception) {
					Log.e("AuthViewModel", "Erreur récupération utilisateur", e)

					_authStatus.update { AuthModel.AuthenticationFailed("Erreur lors de la récupération de l'utilisateur") }
				}
			}
		} else {
			_authStatus.update { AuthModel.Unauthenticated }
		}
	}

	init {
		_auth.addAuthStateListener(_authStateListener)
	}

	override fun onCleared() {
		super.onCleared()
		_auth.removeAuthStateListener(_authStateListener)
	}

	fun setAuthStatus(authStatus: AuthModel) {
		_authStatus.update { authStatus }
	}

	fun setIsLoading(isLoading: Boolean) {
		_isLoading.update { isLoading }
	}

	fun getUserUID(): String? {
		return _auth.currentUser?.uid
	}

	fun getUserById(userUID: String) {
		viewModelScope.launch {
			val user = _repository.getUserById(userUID)
			_user.update { user }
		}
	}

	private fun addUser(user: UserModel) {
		viewModelScope.launch {
			_repository.addUser(user)
		}
	}

	/**
	 * Inscription
	 */
	fun signup(user: UserModel, password: String, onComplete: (Boolean, String?) -> Unit) {
		_auth.createUserWithEmailAndPassword(user.email, password).addOnCompleteListener { task ->
			if (task.isSuccessful) {
				val currentUser = task.result.user

				currentUser?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
					if (verifyTask.isSuccessful) {
						val newUser = UserModel.from(
							uid = currentUser.uid,
							email = user.email,
							firstname = user.firstname,
							lastname = user.lastname
						)

						addUser(newUser)

						logout()

						onComplete(true, null)
					} else {
						onComplete(false, "Une erreur s'est produite lors de l'inscription.")
					}
				}
			} else {
				val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
				val errorMessage: String = when (errorCode) {
					"ERROR_EMAIL_ALREADY_IN_USE" -> {
						"Adresse e-mail déjà utilisée."
					}
					"ERROR_INVALID_EMAIL" -> {
						"Adresse e-mail invalide."
					}
					"ERROR_WEAK_PASSWORD" -> {
						"Le mot de passe doit contenir au moins 6 caractères."
					}
					else -> {
						"Une erreur s'est produite lors de l'inscription."
					}
				}

				onComplete(false, errorMessage)
			}
		}
	}

	/**
	 * Connexion
	 */
	fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
		_auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
			if (task.isSuccessful) {
				val user = task.result.user
				if (user != null && !user.isEmailVerified) {
					logout()
					onComplete(false, "Vous n'avez pas encore validé votre compte.")
				} else {
					val userLogged = task.result.user
					/**
					 * Vérification de la présence de l'utilisateur dans Firestore
					 */
					viewModelScope.launch {
						if (userLogged != null) {
							val userExists = _repository.getUserById(userLogged.uid)

							if (userExists.email.isNotEmpty()) {
								_authStatus.update { AuthModel.Authenticated(userExists) }
								onComplete(true, null)
							} else {
								logout()
								onComplete(false, "Erreur lors de la récupération du compte.")
							}
						}
					}
				}
			} else {
				val errorCode = (task.exception as? FirebaseAuthException)?.errorCode
				val errorMessage: String = when (errorCode) {
					"ERROR_INVALID_EMAIL" -> {
						"Adresse e-mail invalide."
					}
					"ERROR_WRONG_PASSWORD" -> {
						"Mot de passe incorrect."
					}
					"ERROR_USER_NOT_FOUND" -> {
						"Aucun compte trouvé."
					}
					"ERROR_INVALID_CREDENTIAL" -> {
						"Identifiants incorrects."
					}
					else -> {
						"Une erreur s'est produite lors de la connexion."
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
		_auth.signOut()
	}
}