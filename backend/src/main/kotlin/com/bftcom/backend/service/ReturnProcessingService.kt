package com.bftcom.backend.service

import com.bftcom.backend.dto.ReturnMessage
import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.repository.BorrowingRecordRepository
import com.bftcom.backend.repository.ReaderRepository
import com.bftcom.backend.repository.BookRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ReturnProcessingService(
	private val borrowingRecordRepository: BorrowingRecordRepository,
	private val readerRepository: ReaderRepository,
	private val bookRepository: BookRepository,
	private val emailService: EmailService
) {
	private val logger = LoggerFactory.getLogger(ReturnProcessingService::class.java)

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

		val subject = "Подтверждение возврата книги"
		val body = """
            Уважаемый(ая) ${reader.fullName},
            
            Вы успешно вернули книгу "${book.title}" в библиотеку ${returnMessage.returnDate}.
            
            Спасибо за использование наших услуг.
            
            С уважением,
            Ваша Библиотека
        """.trimIndent()

		emailService.sendSimpleMessage(reader.email, subject, body)
		logger.info("Sent return confirmation email to ${reader.email}")
	}
}
