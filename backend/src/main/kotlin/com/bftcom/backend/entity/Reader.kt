package com.bftcom.backend.entity

/**
 * Represents a reader with personal details.
 *
 * @property id Unique identifier; null if not persisted.
 * @property fullName Full name of the reader.
 * @property email Email address of the reader.
 */
data class Reader(
	val id: Long? = null,
	val fullName: String,
	val email: String
)
