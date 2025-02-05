package com.mydigitalschool.nicotrello.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mydigitalschool.nicotrello.data.model.UserModel
import com.mydigitalschool.nicotrello.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel: ViewModel() {
	private val repository = UserRepository()
	private val _user = MutableStateFlow<UserModel?>(null)
	val user: StateFlow<UserModel?> = _user.asStateFlow()

	fun observeUserChanges(uid: String) {
		repository.listenToUserChanges(uid) { user ->
			Log.d("UserViewModel", "observeUserChanges: $user")
			_user.value = user
		}
	}

	fun loadUser(uid: String) {
		viewModelScope.launch {
			val userData = repository.getUser(uid)
			_user.value = userData
		}
	}

	fun saveUser(user: UserModel) {
		viewModelScope.launch {
			repository.saveUser(user)
		}
	}

	override fun onCleared() {
		super.onCleared()
		repository.removeListener()
	}
}