package com.bftcom.backend.model

data class Book(
    val id: Long = 0,
    val workId: Long,
    val title: String,
    val isbn: String,
    val publicationDate: java.time.LocalDate
) {
}