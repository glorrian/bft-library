package com.bftcom.backend.producer

import com.bftcom.backend.config.ArtemisConfig
import com.bftcom.backend.dto.ReturnMessageDto
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.jms.Session
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

/**
 * Sends return messages to a configured JMS queue.
 *
 * Utilizes {@link JmsTemplate} to send a serialized {@link ReturnMessage} to the queue specified
 * by {@link ArtemisConfig.returnQueueName}.
 */
@Component
class ReturnProducer(
	private val jmsTemplate: JmsTemplate,
	private val artemisConfig: ArtemisConfig
) {
	private val objectMapper = jacksonObjectMapper()

	/**
	 * Sends a return message to the configured JMS queue.
	 *
	 * @param borrowingRecordId Identifier of the borrowing record.
	 * @param returnDate The return date as a string.
	 */
	fun sendReturnMessage(borrowingRecordId: Long, returnDate: String) {
		val returnMessageDto = ReturnMessageDto(borrowingRecordId, returnDate)

		jmsTemplate.send(artemisConfig.returnQueueName) { session: Session ->
			val message = session.createTextMessage(objectMapper.writeValueAsString(returnMessageDto))
			message
		}
	}
}
