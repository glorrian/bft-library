package com.bftcom.backend.dto

import java.time.LocalDate

/**
 * DTO for creating a new book entry.
 *
 * @property title Title of the book.
 * @property isbn International Standard Book Number.
 * @property publicationDate Publication date of the book.
 * @property workIds Optional list of associated work IDs.
 */
data class BookCreateDto(
	val title: String,
	val isbn: String,
	val publicationDate: LocalDate,
	val workIds: List<Long>? = null
)

/**
 * DTO for updating an existing book entry.
 *
 * @property title New title of the book, if updating.
 * @property isbn New ISBN of the book, if updating.
 * @property publicationDate New publication date, if updating.
 * @property workIds New list of associated work IDs, if updating.
 */
data class BookUpdateDto(
	val title: String?,
	val isbn: String?,
	val publicationDate: LocalDate?,
	val workIds: List<Long>?
)