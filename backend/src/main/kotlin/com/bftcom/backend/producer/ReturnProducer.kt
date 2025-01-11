package com.bftcom.backend.producer

import com.bftcom.backend.config.ArtemisConfig
import com.bftcom.backend.dto.ReturnMessage
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.jms.Session
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class ReturnProducer(
	private val jmsTemplate: JmsTemplate,
	private val artemisConfig: ArtemisConfig
) {
	private val objectMapper = jacksonObjectMapper()

	fun sendReturnMessage(borrowingRecordId: Long, returnDate: String) {
		val returnMessage = ReturnMessage(borrowingRecordId, returnDate)

		jmsTemplate.send(artemisConfig.returnQueueName) { session: Session ->
			val message = session.createTextMessage(objectMapper.writeValueAsString(returnMessage))
			message
		}
	}
}
