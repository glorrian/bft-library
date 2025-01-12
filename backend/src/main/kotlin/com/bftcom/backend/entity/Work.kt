package com.bftcom.backend.entity

/**
 * Represents a literary work, including its title, genre, and associated authors.
 *
 * @property id Unique identifier; null if not persisted.
 * @property title Title of the work.
 * @property genreId Identifier of the genre; null if not specified.
 * @property authorsIds List of author IDs associated with the work; may be null.
 */
data class Work(
	val id: Long? = null,
	val title: String,
	val genreId: Long?,
	val authorsIds: List<Long>?
)