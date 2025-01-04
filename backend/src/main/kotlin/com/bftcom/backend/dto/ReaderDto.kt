package com.bftcom.backend.dto

data class ReaderCreateDto(
	val fullName: String,
	val email: String
)

data class ReaderUpdateDto(
	val fullName: String?,
	val email: String?
)