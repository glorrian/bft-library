package com.bftcom.backend.service

import com.bftcom.backend.config.LibraryConfig
import com.bftcom.backend.dto.ReminderMessageDto
import com.bftcom.backend.dto.ReturnMessageDto
import com.bftcom.backend.entity.Book
import com.bftcom.backend.entity.BorrowingRecord
import com.bftcom.backend.entity.Reader
import com.bftcom.backend.repository.BookRepository
import com.bftcom.backend.repository.BorrowingRecordRepository
import com.bftcom.backend.repository.ReaderRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate

class EmailProcessingServiceTest {

	private lateinit var borrowingRecordRepository: BorrowingRecordRepository
	private lateinit var readerRepository: ReaderRepository
	private lateinit var bookRepository: BookRepository
	private lateinit var emailService: EmailService
	private lateinit var objectMapper: ObjectMapper
	private lateinit var libraryConfig: LibraryConfig

	private lateinit var service: EmailProcessingService

	@BeforeEach
	fun setup() {
		borrowingRecordRepository = mock()
		readerRepository = mock()
		bookRepository = mock()
		emailService = mock()

		objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule())

		libraryConfig = LibraryConfig().apply {
			maxBorrowingDays = 14L
			emailTemplate = LibraryConfig.EmailTemplate().apply {
				returnMailTemplate = "templates/return_template.json"
				remindMailTemplate = "templates/remind_template.json"
			}
		}

		service = EmailProcessingService(
			borrowingRecordRepository,
			readerRepository,
			bookRepository,
			emailService,
			objectMapper,
			libraryConfig
		)
	}

	@Test
	fun givenReturnMessage_whenDataIsValid_thenUpdateRecordAndSendEmail() {
		val borrowingRecordId = 1L
		val returnDateString = "2023-08-15"
		val returnMessageDto = ReturnMessageDto(borrowingRecordId, returnDateString)

		val borrowingRecord = BorrowingRecord(
			id = borrowingRecordId,
			libraryBookId = 100L,
			readerId = 10L,
			borrowDate = LocalDate.of(2023, 1, 1),
			returnDate = null
		)
		val updatedRecord = borrowingRecord.copy(returnDate = LocalDate.parse(returnDateString))
		val reader = Reader(id = 10L, fullName = "John Doe", email = "john@example.com")
		val book = Book(
			id = 100L,
			title = "Test Book",
			isbn = "1234567890123",
			publicationDate = LocalDate.of(2020, 1, 1),
			worksIds = emptyList()
		)

		whenever(borrowingRecordRepository.findById(borrowingRecordId)).thenReturn(borrowingRecord)
		whenever(readerRepository.findById(borrowingRecord.readerId)).thenReturn(reader)
		whenever(bookRepository.findById(borrowingRecord.libraryBookId)).thenReturn(book)

		service.processReturnMessage(returnMessageDto)

		verify(borrowingRecordRepository).update(updatedRecord)

		val expectedSubject = "Напоминание о задолженности в библиотеке"
		val expectedBody = """
        Уважаемый(ая) ${reader.fullName},
        
        Вы взяли в библиотеке книгу "${book.title}" $returnDateString.
        Напоминаем, что срок возврата книги истёк. Пожалуйста, верните книгу как можно скорее.
        
        С уважением,
        Ваша Библиотека
    	""".trimIndent()

		verify(emailService).sendSimpleMessage(reader.email, expectedSubject, expectedBody)
	}

	@Test
	fun givenReminderMessage_whenDataIsValid_thenSendReminderEmail() {
		val borrowingRecordId = 2L
		val reminderMessageDto = ReminderMessageDto(borrowingRecordId)

		val borrowingRecord = BorrowingRecord(
			id = borrowingRecordId,
			libraryBookId = 200L,
			readerId = 20L,
			borrowDate = LocalDate.of(2023, 7, 1),
			returnDate = null
		)
		val reader = Reader(id = 20L, fullName = "Jane Doe", email = "jane@example.com")
		val book = Book(
			id = 200L,
			title = "Another Book",
			isbn = "9783161484105",
			publicationDate = LocalDate.of(2021, 5, 20),
			worksIds = emptyList()
		)

		whenever(borrowingRecordRepository.findById(borrowingRecordId)).thenReturn(borrowingRecord)
		whenever(readerRepository.findById(borrowingRecord.readerId)).thenReturn(reader)
		whenever(bookRepository.findById(borrowingRecord.libraryBookId)).thenReturn(book)

		service.processRemindMessage(reminderMessageDto)

		val expectedSubject = "Напоминание о задолженности в библиотеке"
		val expectedBody = """
            Уважаемый(ая) ${reader.fullName},
            
            Вы взяли в библиотеке книгу "${book.title}" ${borrowingRecord.borrowDate}.
            Напоминаем, что срок возврата книги истёк. Пожалуйста, верните книгу как можно скорее.
            
            С уважением,
            Ваша Библиотека
        """.trimIndent()

		verify(emailService).sendSimpleMessage(reader.email, expectedSubject, expectedBody)
	}
}
