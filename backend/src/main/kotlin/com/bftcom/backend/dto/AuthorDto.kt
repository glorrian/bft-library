package com.bftcom.backend.dto

import java.time.LocalDate

data class AuthorCreateDto(
	val fullName: String,
	val pseudonym: String?,
	val birthDate: LocalDate
)

data class AuthorUpdateDto(
	val fullName: String?,
	val pseudonym: String?,
	val birthDate: LocalDate?
)