package com.mydigitalschool.nicotrello.data.model

sealed class AuthStatus {
	data object Undefined : AuthStatus()
	data object LoggedIn : AuthStatus()
	data object LoggedOut : AuthStatus()
}