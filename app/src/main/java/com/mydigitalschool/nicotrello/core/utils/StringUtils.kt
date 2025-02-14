package com.mydigitalschool.nicotrello.core.utils

import com.mydigitalschool.nicotrello.data.model.ScreenUserModel

fun getAllRoutes(exclude: List<String> = emptyList()): List<String> {
	return ScreenUserModel::class.sealedSubclasses
		.mapNotNull { it.objectInstance }
		.map { it.route }
		.filterNot { it in exclude }
}

fun String.shouldShowBackButton(): Boolean {
	val excludedRoutes = listOf(ScreenUserModel.Projects.route)
	val routesWithBackButton = getAllRoutes(excludedRoutes)
	return routesWithBackButton.any { this.startsWith(it) }
}

fun String.cap(): String {
	return this.replaceFirstChar { it.uppercase() }
}