package com.bftcom.backend.producer

import com.bftcom.backend.config.ArtemisConfig
import com.bftcom.backend.dto.ReminderMessageDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.jms.Session
import org.springframework.jms.core.JmsTemplate
import org.springframework.stereotype.Component

@Component
class ReminderProducer(
	private val jmsTemplate: JmsTemplate,
	private val objectMapper: ObjectMapper,
	private val artemisConfig: ArtemisConfig
) {

	fun sendReminderMessage(borrowingRecordId: Long, delayDays: Long) {
		val delayMillis = delayDays * 24 * 60 * 60 * 1000
		val reminderMessage = ReminderMessageDto(borrowingRecordId)

		jmsTemplate.send(artemisConfig.reminderQueueName) { session: Session ->
			val message = session.createTextMessage(objectMapper.writeValueAsString(reminderMessage))
			message.setLongProperty("AMQ_SCHEDULED_DELAY", delayMillis)
			message
		}
	}
}
