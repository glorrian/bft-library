package com.bftcom.backend.entity

import java.time.LocalDate

data class BorrowingRecord(
	override var id: Long = 0,
	val libraryBookId: Int,
	val borrowDate: LocalDate
) : Entity