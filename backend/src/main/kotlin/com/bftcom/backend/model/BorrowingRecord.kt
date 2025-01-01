package com.bftcom.backend.model

data class BorrowingRecord(
    val id: Long = 0,
    val libraryBookId: Long,
    val readerId: Long,
    val borrowDate: java.time.LocalDate,
    val returnDate: java.time.LocalDate?
)