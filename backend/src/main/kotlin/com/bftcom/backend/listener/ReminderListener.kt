package com.bftcom.backend.listener

import com.bftcom.backend.dto.ReminderMessageDto
import com.bftcom.backend.repository.BookRepository
import com.bftcom.backend.repository.BorrowingRecordRepository
import com.bftcom.backend.repository.ReaderRepository
import com.bftcom.backend.service.EmailService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.slf4j.LoggerFactory
import org.springframework.jms.annotation.JmsListener
import org.springframework.stereotype.Component

@Component
class ReminderListener(
	private val borrowingRecordRepository: BorrowingRecordRepository,
	private val readerRepository: ReaderRepository,
	private val bookRepository: BookRepository,
	private val emailService: EmailService,
	private val objectMapper: ObjectMapper,
) {
	private val logger = LoggerFactory.getLogger(ReminderListener::class.java)

	@JmsListener(destination = "\${library.artemis.reminder-queue-name}")
	fun receiveMessage(message: String) {

		try {
			val reminderMessage: ReminderMessageDto = objectMapper.readValue(message)
			logger.info("Received reminder message for BorrowingRecord ID: ${reminderMessage.borrowingRecordId}")

			val record = borrowingRecordRepository.findById(reminderMessage.borrowingRecordId)
				?: run {
					logger.warn("BorrowingRecord ID ${reminderMessage.borrowingRecordId} not found.")
					return
				}

			if (record.returnDate != null) {
				logger.info("Book ID ${record.libraryBookId} was returned on ${record.returnDate}. No email sent.")
				return
			}

			val reader = readerRepository.findById(record.readerId)
				?: run {
					logger.warn("Reader ID ${record.readerId} not found.")
					return
				}

			val book = bookRepository.findById(record.libraryBookId)
				?: run {
					logger.warn("Book ID ${record.libraryBookId} not found.")
					return
				}

			val subject = "Напоминание о задолженности в библиотеке"
			val body = """
                Уважаемый(ая) ${reader.fullName},
                
                Вы взяли в библиотеке книгу "${book.title}" ${record.borrowDate}.
                Напоминаем, что срок возврата книги истёк. Пожалуйста, верните книгу как можно скорее.
                
                С уважением,
                Ваша Библиотека
            """.trimIndent()

			emailService.sendSimpleMessage(reader.email, subject, body)
			logger.info("Sent reminder email to ${reader.email}")

		} catch (e: Exception) {
			logger.error("Error processing reminder message: $message", e)
		}
	}
}
