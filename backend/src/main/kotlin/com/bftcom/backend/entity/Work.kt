package com.bftcom.backend.entity

data class Work(
	val id: Long? = null,
	val title: String,
	val genreId: Long?,
	val authorsIds: List<Long>?
)