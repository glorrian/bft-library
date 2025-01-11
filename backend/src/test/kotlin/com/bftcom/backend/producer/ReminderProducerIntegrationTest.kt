package com.bftcom.backend.producer

import com.bftcom.backend.config.ArtemisConfig
import com.bftcom.backend.dto.ReminderMessageDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.jms.TextMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jms.core.JmsTemplate

@SpringBootTest(properties = ["spring.jms.listener.auto-startup=false"])
class ReminderProducerIntegrationTest {

	@Autowired
	lateinit var reminderProducer: ReminderProducer

	@Autowired
	lateinit var jmsTemplate: JmsTemplate

	@Autowired
	lateinit var objectMapper: ObjectMapper

	@Autowired
	lateinit var artemisConfig: ArtemisConfig

	@Test
	fun givenBorrowingRecordIdAndDelay_whenSendReminderMessage_thenMessageInQueue() {
		val borrowingRecordId = 1L
		val delayDays = 0L
		val expectedDelayMillis = delayDays * 24 * 60 * 60 * 1000

		reminderProducer.sendReminderMessage(borrowingRecordId, delayDays)

		val message = jmsTemplate.receive(artemisConfig.reminderQueueName) as? TextMessage
		assertNotNull(message, "Message should not be null")

		val messageText = message!!.text
		val reminderMessageDto = objectMapper.readValue(messageText, ReminderMessageDto::class.java)
		assertEquals(borrowingRecordId, reminderMessageDto.borrowingRecordId)

		val actualDelay = message.getLongProperty("AMQ_SCHEDULED_DELAY")
		assertEquals(expectedDelayMillis, actualDelay, "Delay millis should match expected value")
	}
}
