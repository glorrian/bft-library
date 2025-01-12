package com.bftcom.backend.service

import com.bftcom.backend.config.LibraryConfig
import com.bftcom.backend.dto.ReminderMessageDto
import com.bftcom.backend.dto.ReturnMessageDto
import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.repository.BookRepository
import com.bftcom.backend.repository.BorrowingRecordRepository
import com.bftcom.backend.repository.ReaderRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import org.stringtemplate.v4.ST
import java.time.LocalDate

@Service
class EmailProcessingService(
	private val borrowingRecordRepository: BorrowingRecordRepository,
	private val readerRepository: ReaderRepository,
	private val bookRepository: BookRepository,
	private val emailService: EmailService,
	private val objectMapper: ObjectMapper,
	libraryConfig: LibraryConfig
) {
	private val logger = LoggerFactory.getLogger(EmailProcessingService::class.java)

	private lateinit var returnEmailTemplate: EmailTemplate
	private lateinit var remindEmailTemplate: EmailTemplate
	init {
		returnEmailTemplate = parseEmailTemplate(libraryConfig.emailTemplate.returnMailTemplate)
		remindEmailTemplate = parseEmailTemplate(libraryConfig.emailTemplate.remindMailTemplate)
	}

	fun processRemindMessage(reminderMessageDto: ReminderMessageDto) {
		logger.info("Processing reminder message for BorrowingRecord ID: ${reminderMessageDto.borrowingRecordId}")
		val record = borrowingRecordRepository.findById(reminderMessageDto.borrowingRecordId)
			?: run {
				logger.warn("BorrowingRecord ID ${reminderMessageDto.borrowingRecordId} not found.")
				return
			}

		if (record.returnDate != null) {
			logger.info("Book ID ${record.libraryBookId} was returned on ${record.returnDate}. No email sent.")
			return
		}

		val reader = readerRepository.findById(record.readerId)
			?: run {
				logReaderNotFound(record.readerId)
				return
			}

		val book = bookRepository.findById(record.libraryBookId)
			?: run {
				logBookNotFound(record.libraryBookId)
				return
			}

		val subject = remindEmailTemplate.subject
		val body = ST(remindEmailTemplate.body)
			.add("readerName", reader.fullName)
			.add("bookTitle", book.title)
			.add("returnDate", record.borrowDate.toString())
			.render()

		emailService.sendSimpleMessage(reader.email, subject, body)
		logger.info("Sent reminder email to ${reader.email}")
	}

	fun processReturnMessage(returnMessageDto: ReturnMessageDto) {
		logger.info("Processing return message for BorrowingRecord ID: ${returnMessageDto.borrowingRecordId}, Return Date: ${returnMessageDto.returnDate}")

		val record: BorrowingRecord = borrowingRecordRepository.findById(returnMessageDto.borrowingRecordId)
			?: throw IllegalStateException("BorrowingRecord not found")

		val updatedRecord = record.copy(returnDate = LocalDate.parse(returnMessageDto.returnDate))
		borrowingRecordRepository.update(updatedRecord)

		val reader = readerRepository.findById(record.readerId)
		if (reader == null) {
			logReaderNotFound(record.readerId)
			return
		}

		val book = bookRepository.findById(record.libraryBookId)
		if (book == null) {
			logBookNotFound(record.libraryBookId)
			return
		}

		val subject = returnEmailTemplate.subject
		val body = ST(returnEmailTemplate.body)
			.add("readerName", reader.fullName)
			.add("bookTitle", book.title)
			.add("returnDate", returnMessageDto.returnDate)
			.render()

		emailService.sendSimpleMessage(reader.email, subject, body)
		logger.info("Sent return confirmation email to ${reader.email}")
	}

	private fun logReaderNotFound(readerId: Long) = logger.warn("Reader ID $readerId not found.")
	private fun logBookNotFound(bookId: Long) = logger.warn("Book ID $bookId not found.")

	private fun parseEmailTemplate(templateFilePath: String): EmailTemplate {
		val tempFile = ClassPathResource(templateFilePath).file

		if (tempFile.exists() && tempFile.extension == "json") {
			val emailTemp = objectMapper.readValue(tempFile, EmailTemplate::class.java)
			logger.info("Parsed email template: $emailTemp")
			return emailTemp
		} else {
			throw IllegalArgumentException("Invalid template file: $templateFilePath")
		}
	}

	private data class EmailTemplate(val subject: String, val body: String)
}
