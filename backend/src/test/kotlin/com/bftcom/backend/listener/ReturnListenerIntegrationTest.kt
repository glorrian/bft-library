package com.bftcom.backend.listener

import com.bftcom.backend.config.ArtemisConfig
import com.bftcom.backend.dto.ReturnMessageDto
import com.bftcom.backend.service.EmailProcessingService
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
	lateinit var emailProcessingService: EmailProcessingService

	@Autowired
	lateinit var artemisConfig: ArtemisConfig

	@TestConfiguration
	class Config {
		@Bean
		@Primary
		fun returnProcessingService(): EmailProcessingService = Mockito.mock(EmailProcessingService::class.java)
	}

	@Test
	fun givenValidReturnMessage_whenMessageReceived_thenProcessReturnMessageCalled() {
		val borrowingRecordId = 5L
		val returnDate = "2023-08-15"
		val returnMessageDto = ReturnMessageDto(borrowingRecordId, returnDate)
		val queueName = artemisConfig.returnQueueName

		val messageContent = objectMapper.writeValueAsString(returnMessageDto)

		jmsTemplate.convertAndSend(queueName, messageContent)

		Thread.sleep(1000)

		Mockito.verify(emailProcessingService).processReturnMessage(returnMessageDto)
	}
}
