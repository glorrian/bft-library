package com.bftcom.backend.entity

import java.time.LocalDate

/**
 * Entity representing an author with identification, full name, optional pseudonym, and birthdate.
 *
 * @property id Unique identifier; null if not yet persisted.
 * @property fullName The author's full name.
 * @property pseudonym Optional pen name.
 * @property birthDate The author's date of birth.
 */
data class Author(
	val id: Long? = null,
	val fullName: String,
	val pseudonym: String?,
	val birthDate: LocalDate
)