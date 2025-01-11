package com.bftcom.backend.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EmailService {
	private val logger = LoggerFactory.getLogger(EmailService::class.java)

	fun sendSimpleMessage(to: String, subject: String, text: String) {
		// Стаб: логирование вместо отправки письма
		logger.info("Sending email to: $to")
		logger.info("Subject: $subject")
		logger.info("Body: \n$text")
	}
}