package com.bftcom.backend.entity

import java.time.LocalDate

data class BorrowingRecord(
	val id: Long? = null,
	val libraryBookId: Long,
	val readerId: Long,
	val borrowDate: LocalDate,
	val returnDate: LocalDate? = null
)
