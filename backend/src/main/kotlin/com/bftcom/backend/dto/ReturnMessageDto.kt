package com.bftcom.backend.dto

/**
 * Data Transfer Object representing a return message.
 *
 * Contains the ID of the borrowing record and the date the item was returned.
 *
 * @property borrowingRecordId Unique identifier of the borrowing record.
 * @property returnDate The return date in ISO string format.
 */
data class ReturnMessageDto(
	val borrowingRecordId: Long,
	val returnDate: String
)