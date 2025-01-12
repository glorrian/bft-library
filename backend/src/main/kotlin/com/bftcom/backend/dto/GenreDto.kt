package com.bftcom.backend.dto

/**
 * DTO for creating a new genre.
 *
 * @property name Name of the genre.
 */
data class GenreCreateDto(
	val name: String
)

/**
 * DTO for updating an existing genre.
 *
 * @property name New name of the genre, if updating.
 */
data class GenreUpdateDto(
	val name: String?
)