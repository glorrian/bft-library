package com.bftcom.backend.entity

data class Work(
	override var id: Long = 0,
	val title: String,
	val genreId: Long
) : Entity
