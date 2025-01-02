package com.bftcom.backend.entity

import java.time.LocalDate

data class Author(
	override var id: Long = 0,
	val fullName: String,
	val pseudonym: String? = null,
	val birthDate: LocalDate
) : Entity
