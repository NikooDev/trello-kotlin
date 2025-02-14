package com.mydigitalschool.nicotrello.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun Date.toIsoFormat(): String {
	val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.FRANCE)
	sdf.timeZone = TimeZone.getTimeZone("UTC")
	return sdf.format(this)
}

fun Date.toFormattedString(pattern: String = "dd/MM/yyyy"): String {
	val formatter = SimpleDateFormat(pattern, Locale.getDefault())
	return formatter.format(this)
}