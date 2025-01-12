package com.bftcom.backend.entity

import java.time.LocalDate

/**
 * Represents a book with its metadata and related work identifiers.
 *
 * @property id Unique identifier; null if not yet persisted.
 * @property title Title of the book.
 * @property isbn International Standard Book Number.
 * @property publicationDate Date when the book was published.
 * @property worksIds Optional list of associated work IDs.
 */
data class Book(
	val id: Long? = null,
	val title: String,
	val isbn: String,
	val publicationDate: LocalDate,
	val worksIds: List<Long>? = null
)