package com.bftcom.backend.dto

import java.time.LocalDate

data class BookCreateDto(
	val title: String,
	val isbn: String,
	val publicationDate: LocalDate,
	val workIds: List<Long>? = null
)

data class BookUpdateDto(
	val title: String?,
	val isbn: String?,
	val publicationDate: LocalDate?,
	val workIds: List<Long>?
)