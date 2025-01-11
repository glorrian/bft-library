package com.bftcom.backend.listener

import com.bftcom.backend.dto.ReturnMessage
import com.bftcom.backend.service.ReturnProcessingService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class ReturnListener(
	private val returnProcessingService: ReturnProcessingService,
	private val objectMapper: ObjectMapper,
) {
	private val logger = LoggerFactory.getLogger(ReturnListener::class.java)

	@JmsListener(destination = "\${library.artemis.return-queue-name}")
	fun receiveMessage(message: String) {
		try {
			val returnMessage: ReturnMessage = objectMapper.readValue(message)
			logger.info("Received return message: $returnMessage")
			returnProcessingService.processReturnMessage(returnMessage)
		} catch (e: Exception) {
			logger.error("Error processing return message: $message", e)
		}
	}
}
