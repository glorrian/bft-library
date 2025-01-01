package com.bftcom.backend.model

data class LibraryBookStatus(
    val id: Long = 0,
    val libraryBookId: Long,
    val status: String
)
