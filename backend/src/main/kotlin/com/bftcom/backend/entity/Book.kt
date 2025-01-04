package com.bftcom.backend.entity

import java.time.LocalDate

data class Book(
	val id: Long? = null,
	val title: String,
	val isbn: String,
	val publicationDate: LocalDate,
	val worksIds: List<Long>? = null // Many-to-Many через book_works
)