package com.bftcom.backend.entity

import java.time.LocalDate

data class Author(
	val id: Long? = null,
	val fullName: String,
	val pseudonym: String?,
	val birthDate: LocalDate
)