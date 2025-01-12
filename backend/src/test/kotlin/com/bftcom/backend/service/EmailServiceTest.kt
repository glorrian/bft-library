package com.bftcom.backend.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.util.ReflectionTestUtils

@ExtendWith(MockitoExtension::class)
class EmailServiceTest {

	@Mock
	private lateinit var mailSender: JavaMailSender

	@InjectMocks
	private lateinit var emailService: EmailService

	@BeforeEach
	fun setup() {
		ReflectionTestUtils.setField(emailService, "fromEmail", "test@example.com")
	}

	@Test
	fun givenEmailDetails_whenSendSimpleMessage_thenEmailIsSent() {
		val to = "recipient@example.com"
		val subject = "Test Subject"
		val text = "Test email body"

		emailService.sendSimpleMessage(to, subject, text)

		val messageCaptor = ArgumentCaptor.forClass(SimpleMailMessage::class.java)
		verify(mailSender).send(messageCaptor.capture())
		val sentMessage = messageCaptor.value
		assertEquals(to, sentMessage.to!![0])
		assertEquals(subject, sentMessage.subject)
		assertEquals(text, sentMessage.text)
		assertEquals("test@example.com", sentMessage.from)
	}
}
