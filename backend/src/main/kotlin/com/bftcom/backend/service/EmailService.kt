package com.bftcom.backend.service

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(private val mailSender: JavaMailSender) {
	private val logger = LoggerFactory.getLogger(EmailService::class.java)

	@Value("\${spring.mail.username}")
	private lateinit var fromEmail: String

	fun sendSimpleMessage(to: String, subject: String, text: String) {
		try {
			val message = SimpleMailMessage()
			message.setTo(to)
			message.subject = subject
			message.text = text
			message.from = fromEmail
			mailSender.send(message)
			logger.info("Email sent to: $to with subject: $subject")
		} catch (e: Exception) {
			logger.error("Error sending email", e)
		}
	}
}