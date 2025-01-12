package com.bftcom.backend.dto

import java.time.LocalDate

/**
 * DTO for creating a new author.
 *
 * @property fullName Full name of the author.
 * @property pseudonym Optional pseudonym or pen name.
 * @property birthDate Date of birth of the author.
 */
data class AuthorCreateDto(
	val fullName: String,
	val pseudonym: String?,
	val birthDate: LocalDate
)

/**
 * DTO for updating an existing author's details.
 *
 * @property fullName New full name, if updating.
 * @property pseudonym New pseudonym, if updating.
 * @property birthDate New date of birth, if updating.
 */
data class AuthorUpdateDto(
	val fullName: String?,
	val pseudonym: String?,
	val birthDate: LocalDate?
)