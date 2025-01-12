package com.bftcom.backend.dto

/**
 * Data Transfer Object for a reminder message.
 *
 * Contains the ID of the borrowing record for which a reminder is sent.
 *
 * @property borrowingRecordId Unique identifier of the borrowing record.
 */
data class ReminderMessageDto(
	val borrowingRecordId: Long
)