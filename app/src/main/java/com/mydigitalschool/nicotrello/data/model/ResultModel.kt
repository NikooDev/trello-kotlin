package com.mydigitalschool.nicotrello.data.model

sealed class ResultModel<out T> {
	data class Success<out T>(val data: T) : ResultModel<T>()
	data class Error<U>(val message: String) : ResultModel<U>()
}