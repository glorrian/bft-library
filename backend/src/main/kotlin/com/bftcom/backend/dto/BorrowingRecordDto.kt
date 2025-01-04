package com.bftcom.backend.dto

import java.time.LocalDate

data class BorrowingRecordCreateDto(
	val libraryBookId: Long,
	val borrowDate: LocalDate,
	val returnDate: LocalDate? = null
)

data class BorrowingRecordUpdateDto(
	val libraryBookId: Long?,
	val borrowDate: LocalDate?,
	val returnDate: LocalDate?
)

data class BorrowingRecordDto(
	val borrowingRecordId: Long,
	val libraryBookId: Long,
	val bookTitle: String?,
	val readerId: Long?,
	val readerFullName: String?,
	val borrowDate: LocalDate,
	val returnDate: LocalDate?
)