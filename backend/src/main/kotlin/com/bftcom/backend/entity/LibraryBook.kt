package com.bftcom.backend.entity

/**
 * Represents a library book entry that links to a specific book.
 *
 * @property id Unique identifier; null if not persisted.
 * @property bookId Identifier of the associated book.
 */
data class LibraryBook(
	val id: Long? = null,
	val bookId: Long
)