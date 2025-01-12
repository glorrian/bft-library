package com.bftcom.backend.producer

import com.bftcom.backend.config.ArtemisConfig
import com.bftcom.backend.dto.ReturnMessageDto
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.jms.TextMessage
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jms.core.JmsTemplate

@SpringBootTest(properties = ["spring.jms.listener.auto-startup=false"])
class ReturnProducerIntegrationTest {

	@Autowired
	lateinit var returnProducer: ReturnProducer

	@Autowired
	lateinit var jmsTemplate: JmsTemplate

	@Autowired
	lateinit var objectMapper: ObjectMapper

	@Autowired
	lateinit var artemisConfig: ArtemisConfig

	@Test
	fun givenBorrowingRecordIdAndReturnDate_whenSendReturnMessage_thenMessageInQueue() {
		val borrowingRecordId = 42L
		val returnDate = "2023-08-05"

		returnProducer.sendReturnMessage(borrowingRecordId, returnDate)

		val queueName = artemisConfig.returnQueueName
		val message = jmsTemplate.receive(queueName) as? TextMessage

		assertNotNull(message, "Message should not be null")

		val messageText = message!!.text
		val reminderMessage = objectMapper.readValue(messageText, ReturnMessageDto::class.java)

		assertEquals(borrowingRecordId, reminderMessage.borrowingRecordId)
		assertEquals(returnDate, reminderMessage.returnDate)
	}
}
