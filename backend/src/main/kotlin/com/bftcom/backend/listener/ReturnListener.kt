package com.bftcom.backend.listener

import com.bftcom.backend.dto.ReturnMessageDto
import com.bftcom.backend.service.EmailProcessingService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

/**
 * JMS listener for processing return messages.
 *
 * Listens to a JMS queue specified by the property `library.artemis.return-queue-name`
 * and delegates processing of return messages to {@link ReturnProcessingService}.
 */
@Component
class ReturnListener(
	private val emailProcessingService: EmailProcessingService,
	private val objectMapper: ObjectMapper,
) {
	private val logger = LoggerFactory.getLogger(ReturnListener::class.java)

	/**
	 * Receives and processes a return message from the configured JMS queue.
	 *
	 * @param message The raw JMS message content as a JSON string.
	 */
	@JmsListener(destination = "\${library.artemis.return-queue-name}")
	fun receiveMessage(message: String) {
		try {
			val returnMessageDto: ReturnMessageDto = objectMapper.readValue(message)
			logger.info("Received return message: $returnMessageDto")
			emailProcessingService.processReturnMessage(returnMessageDto)
		} catch (e: Exception) {
			logger.error("Error processing return message: $message", e)
		}
	}
}
