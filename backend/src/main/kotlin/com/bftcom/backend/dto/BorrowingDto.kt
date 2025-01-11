package com.bftcom.backend.dto

import java.time.LocalDate

data class IssueRequest(
	val readerId: Long,
	val libraryBookId: Long,
	val borrowDate: LocalDate
)

data class ReturnRequest(
	val recordId: Long,
	val returnDate: LocalDate
)