package com.bftcom.backend.dto

data class LibraryBookCreateDto(
	val bookId: Long
)

data class LibraryBookUpdateDto(
	val bookId: Long?
)