package com.bftcom.backend.listener

import com.bftcom.backend.dto.ReminderMessageDto
import com.bftcom.backend.service.EmailProcessingService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class ReminderListener(
	private val emailProcessingService: EmailProcessingService,
	private val objectMapper: ObjectMapper,
) {
	private val logger = LoggerFactory.getLogger(ReminderListener::class.java)

	@JmsListener(destination = "\${library.artemis.reminder-queue-name}")
	fun receiveMessage(message: String) {
		try {
			val reminderMessage: ReminderMessageDto = objectMapper.readValue(message)
			logger.info("Received reminder message for BorrowingRecord ID: ${reminderMessage.borrowingRecordId}")
			emailProcessingService.processRemindMessage(reminderMessage)
		} catch (e: Exception) {
			logger.error("Error processing reminder message: $message", e)
		}
	}
}
