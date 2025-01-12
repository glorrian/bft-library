package com.bftcom.backend.dto

import java.time.LocalDate

/**
 * Request DTO for issuing a library book.
 *
 * @property readerId Identifier of the reader borrowing the book.
 * @property libraryBookId Identifier of the library book to be borrowed.
 * @property borrowDate Date when the book is borrowed.
 */
data class IssueRequest(
	val readerId: Long,
	val libraryBookId: Long,
	val borrowDate: LocalDate
)

/**
 * Request DTO for returning a borrowed library book.
 *
 * @property recordId Identifier of the borrowing record.
 * @property returnDate Date when the book is returned.
 */
data class ReturnRequest(
	val recordId: Long,
	val returnDate: LocalDate
)