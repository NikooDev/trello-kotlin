package com.mydigitalschool.nicotrello.data.model

sealed class AuthModel {
	data object Authenticating : AuthModel()
	data class Authenticated(val user: UserModel) : AuthModel()
	data class AuthenticationFailed(val error: String) : AuthModel()
	data object Unauthenticated : AuthModel()
}