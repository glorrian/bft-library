package com.bftcom.backend.dto

/**
 * DTO for creating a new library book record.
 *
 * @property bookId Identifier of the book to be added to the library.
 */
data class LibraryBookCreateDto(
	val bookId: Long
)

/**
 * DTO for updating an existing library book record.
 *
 * @property bookId New book identifier, if updating.
 */
data class LibraryBookUpdateDto(
	val bookId: Long?
)