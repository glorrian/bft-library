package com.bftcom.backend.service

import com.bftcom.backend.config.LibraryConfig
import com.bftcom.backend.dto.ReturnMessage
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
class ReturnProcessingService(
	private val borrowingRecordRepository: BorrowingRecordRepository,
	private val readerRepository: ReaderRepository,
	private val bookRepository: BookRepository,
	private val emailService: EmailService,
	private val objectMapper: ObjectMapper,
	libraryConfig: LibraryConfig
) {
	private val logger = LoggerFactory.getLogger(ReturnProcessingService::class.java)

	private lateinit var emailTemplate: EmailTemplate
	init {
		emailTemplate = parseEmailTemplate(libraryConfig.returnTemplatePath)
	}

	fun processReturnMessage(returnMessage: ReturnMessage) {
		logger.info("Processing return message for BorrowingRecord ID: ${returnMessage.borrowingRecordId}, Return Date: ${returnMessage.returnDate}")

		val record: BorrowingRecord = borrowingRecordRepository.findById(returnMessage.borrowingRecordId)
			?: throw IllegalStateException("BorrowingRecord not found")

		val updatedRecord = record.copy(returnDate = LocalDate.parse(returnMessage.returnDate))
		borrowingRecordRepository.update(updatedRecord)

		val reader = readerRepository.findById(record.readerId)
		if (reader == null) {
			logger.warn("Reader ID ${record.readerId} not found.")
			return
		}

		val book = bookRepository.findById(record.libraryBookId)
		if (book == null) {
			logger.warn("Book ID ${record.libraryBookId} not found.")
			return
		}

		val subject = emailTemplate.subject
		val body = ST(emailTemplate.body)
			.add("readerName", reader.fullName)
			.add("bookTitle", book.title)
			.add("returnDate", returnMessage.returnDate)
			.render()

		emailService.sendSimpleMessage(reader.email, subject, body)
		logger.info("Sent return confirmation email to ${reader.email}")
	}

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
