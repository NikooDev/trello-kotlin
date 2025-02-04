package com.mydigitalschool.nicotrello.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydigitalschool.nicotrello.data.model.AuthStatus
import com.mydigitalschool.nicotrello.manager.AuthManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel(private val authManager: AuthManager) : ViewModel() {
	private val _authStatus = MutableLiveData<AuthStatus>(AuthStatus.Undefined)
	val authStatus: LiveData<AuthStatus> get() = _authStatus

	init {
		authManager.addAuthStateListener()

		authManager.userLiveData.observeForever { user ->
			viewModelScope.launch {
				delay(1000)
				if (user != null) {
					_authStatus.postValue(AuthStatus.LoggedIn)
				} else {
					_authStatus.postValue(AuthStatus.LoggedOut)
				}
			}
		}
	}

	fun signup(email: String, password: String, firstname: String, lastname: String, onComplete: (Boolean, String?) -> Unit) {
		authManager.signup(email, password, firstname, lastname, onComplete)
	}

	fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
		authManager.login(email, password, onComplete)
	}

	fun logout() {
		authManager.logout()
	}

	override fun onCleared() {
		super.onCleared()

		authManager.removeAuthStateListener()
	}
}