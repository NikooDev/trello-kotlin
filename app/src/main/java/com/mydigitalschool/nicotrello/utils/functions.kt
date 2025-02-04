package com.mydigitalschool.nicotrello.utils

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