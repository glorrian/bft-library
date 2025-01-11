package com.bftcom.backend.listener

import com.bftcom.backend.config.ArtemisConfig
import com.bftcom.backend.dto.ReturnMessage
import com.bftcom.backend.service.ReturnProcessingService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.jms.core.JmsTemplate

@SpringBootTest
@Import(ReturnListenerIntegrationTest.Config::class)
class ReturnListenerIntegrationTest {

	@Autowired
	lateinit var jmsTemplate: JmsTemplate

	@Autowired
	lateinit var objectMapper: ObjectMapper

	@Autowired
	lateinit var returnProcessingService: ReturnProcessingService

	@Autowired
	lateinit var artemisConfig: ArtemisConfig

	@TestConfiguration
	class Config {
		@Bean
		@Primary
		fun returnProcessingService(): ReturnProcessingService = Mockito.mock(ReturnProcessingService::class.java)
	}

	@Test
	fun givenValidReturnMessage_whenMessageReceived_thenProcessReturnMessageCalled() {
		val borrowingRecordId = 5L
		val returnDate = "2023-08-15"
		val returnMessage = ReturnMessage(borrowingRecordId, returnDate)
		val queueName = artemisConfig.returnQueueName

		val messageContent = objectMapper.writeValueAsString(returnMessage)

		jmsTemplate.convertAndSend(queueName, messageContent)

		Thread.sleep(1000)

		Mockito.verify(returnProcessingService).processReturnMessage(returnMessage)
	}
}
