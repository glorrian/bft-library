package com.bftcom.backend.entity

import java.time.LocalDate

/**
 * Entity representing a record of a library book borrowing event.
 *
 * @property id Unique identifier of the borrowing record; null if not yet persisted.
 * @property libraryBookId Identifier of the borrowed library book.
 * @property readerId Identifier of the reader who borrowed the book.
 * @property borrowDate The date when the book was borrowed.
 * @property returnDate The date when the book was returned; null if not returned yet.
 */
data class BorrowingRecord(
	val id: Long? = null,
	val libraryBookId: Long,
	val readerId: Long,
	val borrowDate: LocalDate,
	val returnDate: LocalDate? = null
)
