package com.bftcom.backend.dto

/**
 * DTO for creating a new literary work.
 *
 * @property title Title of the work.
 * @property genreId Identifier of the genre for the work.
 * @property authorIds Optional list of author IDs associated with the work.
 */
data class WorkCreateDto(
	val title: String,
	val genreId: Long,
	val authorIds: List<Long>? = null
)

/**
 * DTO for updating an existing literary work.
 *
 * @property title New title of the work, if updating.
 * @property genreId New genre identifier, if updating.
 * @property authorIds New list of author IDs, if updating.
 */
data class WorkUpdateDto(
	val title: String?,
	val genreId: Long?,
	val authorIds: List<Long>?
)