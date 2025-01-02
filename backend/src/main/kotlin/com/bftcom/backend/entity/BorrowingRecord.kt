package com.bftcom.backend.entity

import java.time.LocalDate

data class BorrowingRecord(
	override var id: Long = 0,
	val libraryBookId: Long,
	val borrowDate: LocalDate
) : Entity