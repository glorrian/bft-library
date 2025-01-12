package com.bftcom.backend.dto

/**
 * DTO for creating a new reader.
 *
 * @property fullName Full name of the reader.
 * @property email Email address of the reader.
 */
data class ReaderCreateDto(
	val fullName: String,
	val email: String
)

/**
 * DTO for updating an existing reader.
 *
 * @property fullName New full name of the reader, if updating.
 * @property email New email address of the reader, if updating.
 */
data class ReaderUpdateDto(
	val fullName: String?,
	val email: String?
)