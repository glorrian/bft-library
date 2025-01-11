package com.bftcom.backend.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.test.util.ReflectionTestUtils

class EmailServiceTest {

	private lateinit var emailService: EmailService
	private lateinit var mailSender: JavaMailSender

	@BeforeEach
	fun setup() {
		mailSender = mock(JavaMailSender::class.java)
		emailService = EmailService(mailSender)
		ReflectionTestUtils.setField(emailService, "fromEmail", "test@example.com")
	}

	@Test
	fun testSendSimpleMessage() {
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
