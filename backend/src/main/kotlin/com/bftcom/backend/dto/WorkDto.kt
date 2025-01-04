package com.bftcom.backend.dto

data class WorkCreateDto(
	val title: String,
	val genreId: Long,
	val authorIds: List<Long>? = null
)

data class WorkUpdateDto(
	val title: String?,
	val genreId: Long?,
	val authorIds: List<Long>?
)