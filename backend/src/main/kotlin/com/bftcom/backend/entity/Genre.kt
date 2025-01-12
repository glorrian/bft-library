package com.bftcom.backend.entity

/**
 * Represents a literary genre.
 *
 * @property id Unique identifier; null if not persisted.
 * @property name Name of the genre.
 */
data class Genre(
	val id: Long? = null,
	val name: String
)