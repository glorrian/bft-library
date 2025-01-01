package com.bftcom.backend.model

data class Author(
    val id: Long = 0,
    val fullName: String,
    val pseudonym: String?,
    val birthDate: java.time.LocalDate
)