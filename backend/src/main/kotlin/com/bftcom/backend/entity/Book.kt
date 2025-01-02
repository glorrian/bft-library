package com.bftcom.backend.entity

import java.time.LocalDate

data class Book(
	override var id: Long = 0,
	val title: String,
	val isbn: String,
	val publicationDate: LocalDate
) : Entity