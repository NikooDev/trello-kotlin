package com.mydigitalschool.nicotrello.utils

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun String.cap(): String {
	return this.replaceFirstChar { it.uppercase() }
}

fun Date.toIsoFormat(): String {
	val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE)
	sdf.timeZone = TimeZone.getTimeZone("UTC")
	return sdf.format(this)
}

fun Date.toFormattedString(pattern: String = "dd/MM/yyyy"): String {
	val formatter = SimpleDateFormat(pattern, Locale.getDefault())
	return formatter.format(this)
}

fun Timestamp.toFormattedString(): String {
	return this.toDate().toFormattedString("dd/MM/yyyy")
}